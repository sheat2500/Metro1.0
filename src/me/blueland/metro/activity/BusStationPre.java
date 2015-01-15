package me.blueland.metro.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.blueland.metro.R;
import me.blueland.metro.database.DBAdapter;
import me.blueland.metro.model.BusStationPrediction;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Te
 */

public class BusStationPre extends Activity implements OnStreetViewPanoramaReadyCallback {

    private MenuItem addToCollection;
    private ListView listView;
    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    private List<BusStationPrediction> busStationPredictions;
    // To add to collection;
    private String stationCode;
    private String stationName;
    private String line;
    private double lat;
    private double lon;
    private Intent intent;
    DBAdapter adapter = new DBAdapter(this);

    // show Text if no bus;
    TextView showTextIfNoBus;

    private OnMenuItemClickListener positiveItemClickListener = new OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // TODO Auto-generated method stub
            adapter.open();
            adapter.insertFavourate(line, stationName, stationCode, String.valueOf(lat), String.valueOf(lon),
                    "0");
            item.setOnMenuItemClickListener(negitiveItemClickListener);
            adapter.close();
            item.setIcon(R.drawable.button_deletetocollection);
            return true;
        }
    };

    private OnMenuItemClickListener negitiveItemClickListener = new OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // TODO Auto-generated method stub
            adapter.open();
            adapter.deleteFavourite(stationName);
            item.setOnMenuItemClickListener(positiveItemClickListener);
            adapter.close();
            item.setIcon(R.drawable.button_addtocollection);
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail_station_pre);
        actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        intent = getIntent();

        showTextIfNoBus = (TextView) findViewById(R.id.showIfNoSchedule);

        if (intent.getStringExtra("intent").equals("BusFragment")) {
            initViewFromBusFragment();
        } else {
            initViewFromCollectionFragment();
        }

        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater menuinflater = new MenuInflater(this);
        menuinflater.inflate(R.menu.menu_activity_stationpre, menu);
        addToCollection = menu.findItem(R.id.addToCollection);
        addToCollection.setOnMenuItemClickListener(positiveItemClickListener);
        addToCollection.setIcon(R.drawable.button_addtocollection);
        adapter.open();
        Cursor cursor = adapter.queryFavourate(stationCode);
        if (cursor.getCount() != 0) {
            // same value in the table, so change the menu item
            addToCollection = menu.findItem(R.id.addToCollection);
            addToCollection
                    .setOnMenuItemClickListener(negitiveItemClickListener);
            addToCollection.setIcon(R.drawable.button_deletetocollection);
            adapter.close();
        } else {
            adapter.close();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                // Click the back button, then back to previous activity
                finish();
                break;
            case R.id.addToCollection:
                System.out.println("2");
                break;
            case R.id.refreshList:
                System.out.println("3");
                break;
            case R.id.showMapPath:
                System.out.println("4");
                break;
            default:
                break;
        }
        return true;
    }

    // menu onclick event should be like below. Different from button events
    public boolean refreshList(MenuItem item) {
        new BusController().execute(stationCode);
        // click event: first trigger Listenerning, if return false, transfer to
        // this method
        // if return true, stop conducting
        return true;
    }

    public boolean showMapPath(MenuItem item) {
        System.out.println("before back");
        return true;
    }

    // 逻辑未分离
    public void init() {
        progressDialog.show();
        new BusController().execute(stationCode);
    }

    // Initialize all views and get data from previous for further use
    public void initViewFromBusFragment() {
        // Get busStationCode from previous fragment for requesting
        stationName = intent.getStringExtra("busStationName");
        stationCode = intent.getStringExtra("busStationCode");
        lat = intent.getDoubleExtra("latitude", -1);
        lon = intent.getDoubleExtra("longitude", -1);
        listView = (ListView) findViewById(R.id.showTrainStation);
        // change addToCollection Button state of clickable
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Faster Than Buses!");
    }

    // clicked through listitem from collection fragment.
    public void initViewFromCollectionFragment() {
        line = intent.getStringExtra("line");
        stationCode = intent.getStringExtra("stationCode");
        stationName = intent.getStringExtra("stationName");
        lat = Double.parseDouble(intent.getStringExtra("latitude"));
        lon = Double.parseDouble(intent.getStringExtra("longitude"));
        listView = (ListView) findViewById(R.id.showTrainStation);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Faster Than Buses!");
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(lat, lon));
        System.out.println(lat + "..." + lon);
    }

    // BusController
    protected class BusController extends
            AsyncTask<String, Integer, List<BusStationPrediction>> {

        // extends
        @Override
        protected List<BusStationPrediction> doInBackground(
                String... stationCode) {

            HttpClient httpclient;
            // 请求url
            String url;
            url = "http://api.wmata.com/NextBusService.svc/json/jPredictions?StopID="
                    + stationCode[0] + "&api_key=kfgpmgvfgacx98de9q3xazww";

            // 标示代表 BusStationPrediction
            int index = 1;
            HttpGet httpGet = new HttpGet(url);
            httpclient = new DefaultHttpClient();
            StringBuilder result = new StringBuilder();

            try {
                HttpResponse httpResponse = httpclient.execute(httpGet);
                HttpEntity httpentity = httpResponse.getEntity();
                if (httpentity != null) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(httpentity.getContent()));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println(stationCode[0]);
                    // 数据解析
                    busStationPredictions = parseJSON(result.toString(), index);

                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return busStationPredictions;
        }

        @Override
        protected void onPostExecute(
                List<BusStationPrediction> busStationPredictions) {
            // TODO Auto-generated method stub
            List<Map<String, Object>> listMap;
            BusStationPrediction busStationPrediction;

            listMap = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < busStationPredictions.size(); i++) {
                if ((busStationPrediction = busStationPredictions.get(i)) != null) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("lines", busStationPrediction.getROOTID()
                            .toString());
                    map.put("mins", busStationPrediction.getMIN().toString());
                    map.put("destinationame", busStationPrediction
                            .getDIRECTIONTEXT().toString());
                    listMap.add(map);
                }
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(getApplication(),
                    listMap, R.layout.activity_bus_station_pre_item,
                    new String[]{"lines", "mins", "destinationame"},
                    new int[]{R.id.lineName, R.id.predictionMin,
                            R.id.destinationName});
            // progressDialog dismiss in the onPostExecute method.
            // if refreshing the list, do not have to dismiss the dialog
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            listView.setAdapter(simpleAdapter);


            //Make the note visible
            if(simpleAdapter.isEmpty()){
                showTextIfNoBus.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(busStationPredictions);
        }
    }

    private int drawableSelection(String color) {
        if (color.equals("SV")) {
            return R.drawable.silver;
        } else if (color.equals("GR")) {
            return R.drawable.green;
        } else if (color.equals("RD")) {
            return R.drawable.red;
        } else if (color.equals("YL")) {
            return R.drawable.yellow;
        } else if (color.equals("OR")) {
            return R.drawable.orange;
        } else
            return R.drawable.blue;
    }

    // 解析JSON,并且封装
    private List<BusStationPrediction> parseJSON(String result, int value) {
        /**
         * case 1: RailStationPrediction; case 2:
         */
        switch (value) {
            case 1:

                // Model: railStationPrediction
                BusStationPrediction busStationPrediction;
                try {

                    List<BusStationPrediction> busStationPredictions = new ArrayList<BusStationPrediction>();
                    JSONObject jsonobject = new JSONObject(result);
                    JSONArray jsonarray = jsonobject.getJSONArray("Predictions");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        busStationPrediction = new BusStationPrediction(
                                jsonObject.getString("RouteID"),
                                jsonObject.getString("Minutes"),
                                jsonObject.getString("DirectionText"));
                        busStationPredictions.add(busStationPrediction);
                    }
                    return busStationPredictions;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Add more cases, if having more url requests
            default:
                break;
        }
        return null;
    }
}
