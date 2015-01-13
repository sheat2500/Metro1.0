package me.blueland.metro.fragment;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.blueland.metro.R;
import me.blueland.metro.activity.MainActivity;
import me.blueland.metro.model.RailStation;
import me.blueland.metro.model.RailStationPrediction;


public class NearByFragment extends Fragment {


    ListView listShowNearByStation;
    Location mCurrentLocation;
    String lat;
    String lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // First time to get CurrentLocation
    }

    @Override
    public void onStart() {
        super.onStart();
        mCurrentLocation = getCurrentLocation();
        // Calculate the min distance rail stations, and inflate the listView
        new NearByRailStation().execute(mCurrentLocation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);
        listShowNearByStation = (ListView) v.findViewById(R.id.listShowNearByStation);
        return v;
    }

    public Location getCurrentLocation() {
        mCurrentLocation = ((MainActivity) getActivity()).getmCurrentLocation();
//        lat = String.valueOf(mCurrentLocation.getLatitude());
//        lon = String.valueOf(mCurrentLocation.getLongitude());
        System.out.println(String.valueOf(mCurrentLocation.getLatitude()) + " ,...  " + String.valueOf(mCurrentLocation.getLongitude()));
        return mCurrentLocation;
    }

    protected class NearByRailStation extends AsyncTask<Location, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Location... params) {

            Location mCurrentLocation = params[0];
            Double x = mCurrentLocation.getLatitude();
            Double y = mCurrentLocation.getLongitude();
            String[] x_railStations = getResources().getStringArray(R.array.red_line_station_Latitude);
            String[] y_railStations = getResources().getStringArray(R.array.red_line_station_longitude);
            String[] code = getResources().getStringArray(R.array.red_line_station_code);

            // collect the first three railStation to show
            HashMap<String, Double> hashMap = new HashMap<>();
            ArrayList<String> result_code = new ArrayList<>();

            for (int i = 0; i < x_railStations.length; i++) {
                double distance;
                double x_railStation = Double.parseDouble(x_railStations[i]);
                double y_railStation = Double.parseDouble(y_railStations[i]);
                distance = Math.sqrt(Math.pow(x_railStation - x, 2) + Math.pow(y_railStation - y, 2));
                System.out.println(distance);
                hashMap.put(code[i], distance);
            }
            // Sort the HashMap with the distance;


            // Collect the code[i]

            for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
                result_code.add(entry.getKey());
                System.out.println(entry.getKey() + "/" + entry.getValue());
            }
            return result_code;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, strings);
            listShowNearByStation.setAdapter(arrayAdapter);
        }
    }


}
