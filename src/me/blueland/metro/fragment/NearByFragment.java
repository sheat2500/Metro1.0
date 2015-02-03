package me.blueland.metro.fragment;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import me.blueland.metro.R;
import me.blueland.metro.activity.MainActivity;
import me.blueland.metro.model.Entrance;


public class NearByFragment extends Fragment {


    ListView listShowNearByStation;
    Location mCurrentLocation;
    String lat;
    String lon;
    final String key = "yvxzjc8fjhj3pgatt2kxqdab";

    final String DESCRIPTION = "Description";
    final String ID = "ID";
    final String LAT = "Lat";
    final String LON = "Lon";
    final String NAME = "Name";
    final String STATIONCODE1 = "StationCode1";
    final String STATIONCODE2 = "StationCode2";

    List<Map<String, Object>> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // First time to get CurrentLocation
    }

    @Override
    public void onStart() {
        super.onStart();
        // Calculate the min distance rail stations, and inflate the listView
        if ((mCurrentLocation = getCurrentLocation()) != null) {
            new NearByRailStation().execute(mCurrentLocation);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);
        listShowNearByStation = (ListView) v.findViewById(R.id.listShowNearByStation);

        // Show Entrance in the Google Map
        listShowNearByStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = list.get(position);
                Toast.makeText(getActivity(), "showMap", Toast.LENGTH_SHORT)
                        .show();
                //Pop Up Google Map Service
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(map.get(LAT).toString()), Double.parseDouble(map.get(LON).toString()));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);

                //Jump to RailStationPre Activity

            }
        });
        return v;
    }

    public Location getCurrentLocation() {
        mCurrentLocation = ((MainActivity) getActivity()).getmCurrentLocation();
        if (mCurrentLocation != null) {
            lat = String.valueOf(mCurrentLocation.getLatitude());
            lon = String.valueOf(mCurrentLocation.getLongitude());
            return mCurrentLocation;
        }
        return null;
    }

    protected class NearByRailStation extends AsyncTask<Location, Integer, ArrayList<Entrance>> {

        @Override
        protected ArrayList<Entrance> doInBackground(Location... params) {

            Location mCurrentLocation = params[0];


            double x = mCurrentLocation.getLatitude();
            double y = mCurrentLocation.getLongitude();

            double radius = 500;

//            // Test
//            double x, y;
//            x = 38.8978168;
//            y = -77.0404246;

            try {
                URL url = new URL("https://api.wmata.com/Rail.svc/json/jStationEntrances?Lat="
                        + String.valueOf(x) + "&Lon=" + String.valueOf(y)
                        + "&Radius=" + String.valueOf(radius) + "&api_key=" + key);
                System.out.println(url.toString());
                HttpURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                // Params for reading contents
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

                System.out.println(result);

                ArrayList<Entrance> nearByRailEntrances = parseJSON(result.toString());

                return nearByRailEntrances;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Entrance> entrances) {
            super.onPostExecute(entrances);

            if (entrances == null) {
                Toast.makeText(getActivity(), "Internet", Toast.LENGTH_SHORT).show();
                return;
            }

            list = new ArrayList<>();

            for (int i = 0; i < entrances.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                Entrance entrance = entrances.get(i);
                map.put(DESCRIPTION, entrance.getDescription());
                map.put(ID, entrance.getID());
                map.put(LAT, entrance.getLat());
                map.put(LON, entrance.getLon());
                map.put(NAME, entrance.getName());
                map.put(STATIONCODE1, entrance.getStationCode1());
                map.put(STATIONCODE2, entrance.getStationCode2());
                list.add(map);
            }

            String[] param = new String[]{
                    STATIONCODE1, DESCRIPTION
            };

            int[] id = new int[]{
                    R.id.stationCode, R.id.description
            };
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.fragment_nearby_rail_station_item, param, id);
            listShowNearByStation.setAdapter(simpleAdapter);
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, strings);
//            listShowNearByStation.setAdapter(arrayAdapter);
        }
    }

    private ArrayList<Entrance> parseJSON(String result) {

        ArrayList<Entrance> nearByRailStations = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Entrances");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Entrance entrance = new Entrance(object.getString(DESCRIPTION), object.getString(ID), object.getString(LAT), object.getString(LON), object.getString(NAME), object.getString(STATIONCODE1), object.getString(STATIONCODE2));
                nearByRailStations.add(entrance);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nearByRailStations;
    }

}
