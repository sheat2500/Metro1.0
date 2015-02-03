package me.blueland.metro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.blueland.metro.R;
import me.blueland.metro.model.RailStation;

/**
 * Apply in RailFragment
 */
public class RailStationsAdapter extends ArrayAdapter<RailStation> {

    Context context;
    int resource;
    ArrayList<RailStation> al_railStations;
    TextView stationCode;
    TextView stationName;
    TextView stationAddress;

    public RailStationsAdapter(Context context, int resource,
                               ArrayList<RailStation> al_railStations) {
        super(context, resource, al_railStations);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.resource = resource;
        this.al_railStations = al_railStations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(resource, null);
        stationCode = (TextView) v.findViewById(R.id.stationCode);
        stationName = (TextView) v.findViewById(R.id.stationName);
        stationAddress = (TextView) v.findViewById(R.id.stationAddress);
        stationCode.setText(al_railStations.get(position).getStationCode());
        stationName.setText(al_railStations.get(position).getStationName());
        stationAddress.setText(al_railStations.get(position).getAddress());
        return v;
    }
}
