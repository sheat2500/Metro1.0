package me.blueland.metro.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.blueland.metro.R;
import me.blueland.metro.adapter.BusStationAdapter;
import me.blueland.metro.database.DBAdapter;
import me.blueland.metro.model.BusRoute;
import me.blueland.metro.model.BusStation;
import me.blueland.metro.model.BusStationPrediction;

/**
 * @author Te
 */

public class BusStationPre extends Activity implements OnStreetViewPanoramaReadyCallback {

    private Menu mMenu;
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


    // SlidingMenu
    ListView menu_left_lv;
    ListView menu_right_lv;


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
        setContentView(R.layout.activity_bus_station_pre);
        actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        intent = getIntent();

        menu_left_lv = (ListView) findViewById(R.id.menu_left_lv);
        menu_right_lv = (ListView) findViewById(R.id.menu_right_lv);

        SlidingMenu menu_left = new SlidingMenu(this);
        menu_left.setMode(SlidingMenu.LEFT);
        menu_left.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu_left.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        menu_left.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu_left.setFadeDegree(0.35f);
        menu_left.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu_left.setMenu(R.layout.menu_left);


        SlidingMenu menu_right = new SlidingMenu(this);
        menu_right.setMode(SlidingMenu.RIGHT);
        menu_right.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu_right.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        menu_right.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu_right.setFadeDegree(0.35f);
        menu_right.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu_right.setMenu(R.layout.menu_right);


        showTextIfNoBus = (TextView) findViewById(R.id.showIfNoSchedule);

        initView();

        if (intent.getStringExtra("intent").equals("BusFragment")) {
            initViewFromBusFragment();
        } else {
            initViewFromCollectionFragment();
        }

        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        init();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.showTrainStation);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Faster Than Buses!");

        String routeId = ((BusRoute) intent.getSerializableExtra("busRoute")).getRouteId();
        new GetBusStations().execute("https://api.wmata.com/Bus.svc/json/jRouteSchedule?RouteID=" + routeId + "&api_key=yvxzjc8fjhj3pgatt2kxqdab");
    }

    private class GetBusStations extends AsyncTask<String, Void, Map<String, ArrayList<BusStation>>> {


        @Override
        protected Map<String, ArrayList<BusStation>> doInBackground(String... params) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            // Store JSON Analysis Result;
            ArrayList<BusStation> mBusStation_direction0 = new ArrayList<>();
            ArrayList<BusStation> mBusStation_direction1 = new ArrayList<>();
            Map<String, ArrayList<BusStation>> map_2directions_busStations = new HashMap<>();

            try {

                URL url = new URL(params[0]);
                InputStream is = url.openConnection().getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
                System.out.println(stringBuilder.toString());

                // JSON analysis
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                // Direction 0;
                JSONArray jsonArray_D0 = jsonObject.getJSONArray("Direction0");
                JSONArray jsonArray_stopTimes_D0 = (jsonArray_D0.getJSONObject(0).getJSONArray("StopTimes"));
                for (int i = 0; i < jsonArray_stopTimes_D0.length(); i++) {
                    BusStation mBusStation = new BusStation(jsonArray_stopTimes_D0.getJSONObject(i).getString("StopID"), jsonArray_stopTimes_D0.getJSONObject(i).getString("StopName"));
                    mBusStation_direction0.add(mBusStation);
                }
                map_2directions_busStations.put("Direction0", mBusStation_direction0);

                // Direction 1;
                JSONArray jsonArray_D1 = jsonObject.getJSONArray("Direction1");
                JSONArray jsonArray_stopTimes_D1 = (jsonArray_D1.getJSONObject(0).getJSONArray("StopTimes"));
                for (int i = 0; i < jsonArray_stopTimes_D1.length(); i++) {
                    BusStation mBusStation = new BusStation(jsonArray_stopTimes_D1.getJSONObject(i).getString("StopID"), jsonArray_stopTimes_D1.getJSONObject(i).getString("StopName"));
                    mBusStation_direction0.add(mBusStation);
                }
                map_2directions_busStations.put("Direction1", mBusStation_direction1);

                return map_2directions_busStations;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Map<String, ArrayList<BusStation>> stringArrayListMap) {
            super.onPostExecute(stringArrayListMap);
            BusStationAdapter slidingMenu_left = new BusStationAdapter(getApplicationContext(), R.layout.slidingmenu_listview_item, stringArrayListMap.get("Direction0"));
            menu_left_lv.setAdapter(slidingMenu_left);
            BusStationAdapter slidingMenu_right = new BusStationAdapter(getApplicationContext(), R.layout.slidingmenu_listview_item, stringArrayListMap.get("Direction1"));
            menu_left_lv.setAdapter(slidingMenu_right);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        mMenu = menu;
        MenuInflater menuinflater = new MenuInflater(this);
        menuinflater.inflate(R.menu.menu_activity_stationpre, menu);
        addToCollection = menu.findItem(R.id.addToCollection);
        addToCollection.setOnMenuItemClickListener(positiveItemClickListener);
        addToCollection.setIcon(R.drawable.button_addtocollection);

        //renderCollectionIcon();

        return super.onCreateOptionsMenu(menu);
    }

    private void renderCollectionIcon() {
        adapter.open();
        Cursor cursor = adapter.queryFavourate(stationCode);
        if (cursor.getCount() != 0) {
            // same value in the table, so change the menu item
            addToCollection = mMenu.findItem(R.id.addToCollection);
            addToCollection
                    .setOnMenuItemClickListener(negitiveItemClickListener);
            addToCollection.setIcon(R.drawable.button_deletetocollection);
            adapter.close();
        } else {
            adapter.close();
        }

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
        return true;
    }

    public boolean showMapPath(MenuItem item) {
        Toast.makeText(getApplicationContext(), "showMap", Toast.LENGTH_SHORT)
                .show();
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lon);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        this.startActivity(intent);
        return true;
    }

    public void init() {
        progressDialog.show();
//        new BusController().execute(stationCode);

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

            String url;
            url = "http://api.wmata.com/NextBusService.svc/json/jPredictions?StopID="
                    + stationCode[0] + "&api_key=yvxzjc8fjhj3pgatt2kxqdab";

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
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println(stationCode[0]);

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
            if (simpleAdapter.isEmpty()) {
                showTextIfNoBus.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(busStationPredictions);
        }
    }

    private List<BusStationPrediction> parseJSON(String result, int value) {
        switch (value) {
            case 1:

                // Model: BusStationPrediction
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

    public void callUber(View v) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.ubercar", PackageManager.GET_ACTIVITIES);
            Intent launchIntent = pm.getLaunchIntentForPackage("com.ubercab");
            intent.setData(Uri.parse("uber://?action=setPickup&pickup=my_location"));
            startActivity(launchIntent);
        } catch (PackageManager.NameNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.uber.com/sign-up?client_id=hZnIwqdtvTNknw8vnJ8DQFIkzHCDn40W"));
            startActivity(intent);
            e.printStackTrace();
        }
    }
}
