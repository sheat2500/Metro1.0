package me.blueland.metro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.blueland.metro.MetroApplication;
import me.blueland.metro.R;
import me.blueland.metro.adapter.RailStationsAdapter;
import me.blueland.metro.model.RailStation;

public class RailFragment extends Fragment implements Button.OnClickListener {

    private ListView lv_showStations;
    private SimpleAdapter SimpleAdapter;
    private List<Map<String, Object>> listMap;
    // get the clicked button showing which line for the next activity
    String line = "BLUE";
    private Button blue, red, green, yellow, silver, orange;

    // to get data from application
    private Map<String, ArrayList> map_line;
    private ArrayList<RailStation> railStations;

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(
                    "me.blueland.metro.activity.RailStationPre");
            // transfer 5 parameters for the next activity
            // postion for getting specific latitude and longitude in the
            // next activity
            RailStation clickedStation = railStations.get(position);
            intent.putExtra("intent", "RailFragment");
            intent.putExtra("line", line);
            intent.putExtra("position", position);
            intent.putExtra("stationCode", clickedStation.getStationCode());
            intent.putExtra("stationName", clickedStation.getStationName());
            intent.putExtra("latitude", clickedStation.getLatitude());
            intent.putExtra("longitude", clickedStation.getLongitude());
            startActivity(intent);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.fragment_rail, container, false);

        blue = (Button) v.findViewById(R.id.blue_line);
        red = (Button) v.findViewById(R.id.red_line);
        green = (Button) v.findViewById(R.id.green_line);
        yellow = (Button) v.findViewById(R.id.yellow_line);
        silver = (Button) v.findViewById(R.id.silver_line);
        orange = (Button) v.findViewById(R.id.orange_line);

        blue.setOnClickListener(this);
        red.setOnClickListener(this);
        green.setOnClickListener(this);
        yellow.setOnClickListener(this);
        silver.setOnClickListener(this);
        orange.setOnClickListener(this);

        map_line = MetroApplication.getInstance().getLine_RailStaton();

        lv_showStations = (ListView) v.findViewById(R.id.showTrainStation);

        lv_showStations.setOnItemClickListener(onItemClickListener);

        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        blue.callOnClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.red_line:
                railStations = map_line.get("RD");
                break;
            case R.id.blue_line:
                railStations = map_line.get("BL");
                break;
            case R.id.orange_line:
                railStations = map_line.get("OR");
                break;
            case R.id.silver_line:
                railStations = map_line.get("SL");
                break;
            case R.id.green_line:
                railStations = map_line.get("GR");
                break;
            case R.id.yellow_line:
                railStations = map_line.get("YL");
                break;
        }
        RailStationsAdapter railStationsAdapter = new RailStationsAdapter(getActivity(), R.layout.fragment_rail_item, railStations);
        lv_showStations.setAdapter(railStationsAdapter);
    }
}
