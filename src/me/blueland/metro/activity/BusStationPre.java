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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

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
import java.util.Locale;
import java.util.Map;

import me.blueland.metro.R;
import me.blueland.metro.adapter.BusStationAdapter;
import me.blueland.metro.database.DBAdapter;
import me.blueland.metro.model.BusRoute;
import me.blueland.metro.model.BusStation;

/**
 * @author Te
 */

public class BusStationPre extends Activity implements OnStreetViewPanoramaReadyCallback {

    private BusStationAdapter busStationAdapter_d0;
    private BusStationAdapter busStationAdapter_d1;
    private Menu mMenu;
    private MenuItem addToCollection;
    private ListView busStopList;
    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    // To add to collection;
    private String stationCode;
    private String stationName;
    private String line;
    private double lat;
    private double lon;
    private Intent intent;
    DBAdapter adapter = new DBAdapter(this);

    // Out View of Google Street View
    RelativeLayout streetViewWrapper;

    // SlidingMenu
    private SlidingMenu menu_left;
    private ListView menu_left_lv;

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            progressDialog.show();
            String stopId = view.getTag().toString();
            new GetBusSchedule().execute("https://api.wmata.com/NextBusService.svc/json/jPredictions?StopID=" + stopId + "&api_key=2e9e89d29587430980e7b6e2994bf93e");
            new GetStopInfo().execute("https://api.wmata.com/Bus.svc/json/jStopSchedule?StopID=" + stopId + "&api_key=2e9e89d29587430980e7b6e2994bf93e");
        }
    };

    // show Text if no bus;
    TextView showTextIfNoBus;

    private ToggleButton mToggleButton;

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

        menu_left = new SlidingMenu(this);
        menu_left.setMode(SlidingMenu.LEFT);
        menu_left.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu_left.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        menu_left.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu_left.setFadeEnabled(true);
        menu_left.setFadeDegree(0.35f);
        menu_left.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu_left.setMenu(R.layout.menu_left);
        menu_left.showMenu();
        menu_left_lv = (ListView) menu_left.findViewById(R.id.menu_left_lv);
        mToggleButton = (ToggleButton) menu_left.findViewById(R.id.togglebutton);

        menu_left_lv.setOnItemClickListener(onItemClickListener);

        busStopList = (ListView) findViewById(R.id.showTrainStation);

        streetViewWrapper = (RelativeLayout) findViewById(R.id.steetViewWrapper);

        showTextIfNoBus = (TextView) findViewById(R.id.showIfNoSchedule);


        initView();

        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

    }

    private void initView() {
        busStopList = (ListView) findViewById(R.id.showTrainStation);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Faster Than Buses!");
        progressDialog.show();
        String routeId = ((BusRoute) intent.getSerializableExtra("busRoute")).getRouteId();
        new GetBusStations().execute("https://api.wmata.com/Bus.svc/json/jRouteSchedule?RouteID=" + routeId + "&api_key=2e9e89d29587430980e7b6e2994bf93e");
    }

    private class GetStopInfo extends AsyncTask<String, Void, BusStation> {

        @Override
        protected BusStation doInBackground(String... params) {
            BusStation mBusStation;
            StringBuilder mStringBuilder = new StringBuilder();
            String line;
            try {
                URL url = new URL(params[0]);
                InputStream is = url.openConnection().getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    mStringBuilder.append(line);
                }

                JSONObject jsonObject = new JSONObject(mStringBuilder.toString());
                JSONObject jsonO = jsonObject.getJSONObject("Stop");
                mBusStation = new BusStation(jsonO.getString("StopID"), jsonO.getString("Name"), jsonO.getString("Lat"), jsonO.getString("Lon"));

                return mBusStation;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetBusStations extends AsyncTask<String, Void, Map<String, ArrayList<BusStation>>> {

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        ArrayList<BusStation> mBusStation_direction0 = new ArrayList<>();
        ArrayList<BusStation> mBusStation_direction1 = new ArrayList<>();

        @Override
        protected Map<String, ArrayList<BusStation>> doInBackground(String... params) {

            try {

                URL url = new URL(params[0]);
                InputStream is = url.openConnection().getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }

                System.out.println(stringBuilder.toString());

                return parseJSONObject(stringBuilder.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected Map<String, ArrayList<BusStation>> parseJSONObject(String result) {

            Map<String, ArrayList<BusStation>> map_2directions_busStations = new HashMap<>();
            // JSON analysis
            try {
                JSONObject jsonObject = new JSONObject(result);
                // Direction 0;
                JSONArray jsonArray_D0 = jsonObject.getJSONArray("Direction0");
                JSONArray jsonArray_D1 = jsonObject.getJSONArray("Direction1");

                if (jsonArray_D0.length() == 0) {
                    System.out.println("direction0 = null");
                } else {
                    JSONObject jsonObject1 = jsonArray_D0.getJSONObject(0);
                    JSONArray jsonArray_stopTimes_D0 = (jsonObject1.getJSONArray("StopTimes"));
                    for (int i = 0; i < jsonArray_stopTimes_D0.length(); i++) {
                        BusStation mBusStation = new BusStation(jsonArray_stopTimes_D0.getJSONObject(i).getString("StopID"), jsonArray_stopTimes_D0.getJSONObject(i).getString("StopName"));
                        mBusStation_direction0.add(mBusStation);
                    }
                    map_2directions_busStations.put("Direction0", mBusStation_direction0);
                }

                // Direction 1;
                if (jsonArray_D1.length() == 0) {
                    System.out.println("direction1 = null");
                } else {
                    JSONObject jsonObject2 = jsonArray_D1.getJSONObject(0);
                    JSONArray jsonArray_stopTimes_D1 = (jsonObject2.getJSONArray("StopTimes"));
                    for (int i = 0; i < jsonArray_stopTimes_D1.length(); i++) {
                        BusStation mBusStation = new BusStation(jsonArray_stopTimes_D1.getJSONObject(i).getString("StopID"), jsonArray_stopTimes_D1.getJSONObject(i).getString("StopName"));
                        mBusStation_direction1.add(mBusStation);
                    }
                    map_2directions_busStations.put("Direction1", mBusStation_direction1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return map_2directions_busStations;
        }


        @Override
        protected void onPostExecute(Map<String, ArrayList<BusStation>> stringArrayListMap) {

            if (stringArrayListMap.get("Direction1") != null) {
                busStationAdapter_d1 = new BusStationAdapter(getApplicationContext(), R.layout.slidingmenu_listview_item, stringArrayListMap.get("Direction1"));
            } else {
                busStationAdapter_d1 = null;
            }

            if (stringArrayListMap.get("Direction0") != null) {
                busStationAdapter_d0 = new BusStationAdapter(getApplicationContext(), R.layout.slidingmenu_listview_item, stringArrayListMap.get("Direction0"));
            } else {
                busStationAdapter_d0 = null;
            }

            if (busStationAdapter_d1 == null && busStationAdapter_d0 == null) {
                // No bus available

            } else if (busStationAdapter_d0 == null) {
                menu_left_lv.setAdapter(busStationAdapter_d1);
                System.out.println("d0 = null");
            } else if (busStationAdapter_d1 == null) {
                menu_left_lv.setAdapter(busStationAdapter_d0);
                mToggleButton.setChecked(false);
                System.out.println("d1 = null");
            } else {
                menu_left_lv.setAdapter(busStationAdapter_d1);
            }
            progressDialog.dismiss();
            super.onPostExecute(stringArrayListMap);
        }
    }

    private class GetBusSchedule extends AsyncTask<String, Void, ArrayList<Map<String, Object>>> {

        ArrayList<Map<String, Object>> mArrayListMap = new ArrayList<>();

        @Override
        protected ArrayList<Map<String, Object>> doInBackground(String... params) {

            StringBuilder result = new StringBuilder();
            String line;

            try {
                URL url = new URL(params[0]);
                InputStream is = url.openConnection().getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }

                System.out.println(result.toString());

                JSONObject jsonObject = new JSONObject(result.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("Predictions");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Map<String, Object> mMap = new HashMap<>();
                    mMap.put("DirectionNumber", jsonObject1.getString("DirectionNum").equals(0) ? "→" : "←");
                    mMap.put("DirectionText", jsonObject1.getString("DirectionText"));
                    mMap.put("Minutes", jsonObject1.getString("Minutes"));
                    mArrayListMap.add(mMap);
                }
                br.close();
                return mArrayListMap;
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
        protected void onPostExecute(ArrayList<Map<String, Object>> maps) {

            String[] from = new String[]{"DirectionNumber", "DirectionText", "Minutes"};
            int[] to = new int[]{R.id.busDirection, R.id.busName, R.id.busMinute};
            SimpleAdapter mBusStopSimpleAdapter = new SimpleAdapter(getApplicationContext(), maps, R.layout.activity_bus_station_pre_item, from, to);
            busStopList.setAdapter(mBusStopSimpleAdapter);
            progressDialog.dismiss();
            menu_left.toggle();
            streetViewWrapper.setVisibility(View.VISIBLE);
            super.onPostExecute(maps);
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
        }
        return true;
    }

    // menu onclick event should be like below. Different from button events
    public boolean refreshList(MenuItem item) {
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

    // Initialize all views and get data from previous for further use
    public void initViewFromBusFragment() {
        // Get busStationCode from previous fragment for requesting
        stationName = intent.getStringExtra("busStationName");
        stationCode = intent.getStringExtra("busStationCode");
        lat = intent.getDoubleExtra("latitude", -1);
        lon = intent.getDoubleExtra("longitude", -1);
        busStopList = (ListView) findViewById(R.id.showTrainStation);
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
        busStopList = (ListView) findViewById(R.id.showTrainStation);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Faster Than Buses!");
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(lat, lon));
        System.out.println(lat + "..." + lon);
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

    public void onToggleClicked(View v) {
        boolean on = ((ToggleButton) v).isChecked();
        if (on) {
            // Enable vibrate
            menu_left_lv.setAdapter(busStationAdapter_d0);
        } else {
            // Disable vibrate
            menu_left_lv.setAdapter(busStationAdapter_d1);
        }
    }
}
