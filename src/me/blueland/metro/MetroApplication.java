package me.blueland.metro;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.blueland.metro.model.BusRoute;
import me.blueland.metro.model.RailStation;

/**
 * Created by Te on 2/2/15.
 */
public class MetroApplication extends Application {

    private static ArrayList<BusRoute> mArrayListBusStops;

    /*
        Params:
            String : RD , YL
            ArrayList<RailStation> : All stations in a certain Line
     */

    private Map<String, ArrayList> line_RailStaton = new HashMap<>();

    private static MetroApplication mMetroApplication;

    public static MetroApplication getInstance() {
        return mMetroApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMetroApplication = this;
        initialBusData();
        initialRailData();
    }

    private void initialBusData() {
        // BusStop
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("busStop")));
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("Routes");
            mArrayListBusStops = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                BusRoute busRoute = new BusRoute(jsonArray.getJSONObject(i).getString("RouteID"),jsonArray.getJSONObject(i).getString("Name"));
                mArrayListBusStops.add(busRoute);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initialRailData() {

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("railStation")));
            while ((line = mBufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject mJsonObject = new JSONObject(stringBuilder.toString());
            mBufferedReader.close();

            // Red Line
            JSONArray mJsonArray = mJsonObject.getJSONArray("RD");
            ArrayList<RailStation> RD_stations = new ArrayList<>();
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject station = mJsonArray.getJSONObject(i);
                RailStation mRailStation = new RailStation(station.getString("Code"), station.getString("Name"), station.getString("Lat"), station.getString("Lon"), station.getJSONObject("Address").getString("Street") + " " + station.getJSONObject("Address").getString("State"));
                RD_stations.add(mRailStation);
            }
            line_RailStaton.put("RD", RD_stations);

            // Yellow Line
            mJsonArray = mJsonObject.getJSONArray("YL");
            ArrayList<RailStation> YL_stations = new ArrayList<>();
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject station = mJsonArray.getJSONObject(i);
                RailStation mRailStation = new RailStation(station.getString("Code"), station.getString("Name"), station.getString("Lat"), station.getString("Lon"), station.getJSONObject("Address").getString("Street") + " " + station.getJSONObject("Address").getString("State"));
                YL_stations.add(mRailStation);
            }
            line_RailStaton.put("YL", YL_stations);

            // Blue Line
            mJsonArray = mJsonObject.getJSONArray("BL");
            ArrayList<RailStation> BL_stations = new ArrayList<>();
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject station = mJsonArray.getJSONObject(i);
                RailStation mRailStation = new RailStation(station.getString("Code"), station.getString("Name"), station.getString("Lat"), station.getString("Lon"), station.getJSONObject("Address").getString("Street") + " " + station.getJSONObject("Address").getString("State"));
                BL_stations.add(mRailStation);
            }
            line_RailStaton.put("BL", BL_stations);

            // Orange Line
            mJsonArray = mJsonObject.getJSONArray("OR");
            ArrayList<RailStation> OR_stations = new ArrayList<>();
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject station = mJsonArray.getJSONObject(i);
                RailStation mRailStation = new RailStation(station.getString("Code"), station.getString("Name"), station.getString("Lat"), station.getString("Lon"), station.getJSONObject("Address").getString("Street") + " " + station.getJSONObject("Address").getString("State"));
                OR_stations.add(mRailStation);
            }
            line_RailStaton.put("OR", OR_stations);

            // Silver Line
            mJsonArray = mJsonObject.getJSONArray("SV");
            ArrayList<RailStation> SV_stations = new ArrayList<>();
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject station = mJsonArray.getJSONObject(i);
                RailStation mRailStation = new RailStation(station.getString("Code"), station.getString("Name"), station.getString("Lat"), station.getString("Lon"), station.getJSONObject("Address").getString("Street") + " " + station.getJSONObject("Address").getString("State"));
                SV_stations.add(mRailStation);
            }
            line_RailStaton.put("SV", SV_stations);
            // Silver Line
            mJsonArray = mJsonObject.getJSONArray("GR");
            ArrayList<RailStation> GR_stations = new ArrayList<>();
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject station = mJsonArray.getJSONObject(i);
                RailStation mRailStation = new RailStation(station.getString("Code"), station.getString("Name"), station.getString("Lat"), station.getString("Lon"), station.getJSONObject("Address").getString("Street") + " " + station.getJSONObject("Address").getString("State"));
                GR_stations.add(mRailStation);
            }
            line_RailStaton.put("GR", GR_stations);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BusRoute> getmArrayListBusStops() {
        return mArrayListBusStops;
    }

    public Map<String, ArrayList> getLine_RailStaton() {
        return line_RailStaton;
    }

}
