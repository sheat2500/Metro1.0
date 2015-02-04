package me.blueland.metro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import me.blueland.metro.R;
import me.blueland.metro.model.BusStation;

/**
 * Created by Te on 2/4/15.
 */
public class BusStationAdapter extends ArrayAdapter<BusStation> {


    private Context mContext;
    private int mResource;
    private List<BusStation> mBusStations;

    public BusStationAdapter(Context context, int resource, List<BusStation> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mBusStations = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(mResource, parent, false);
        TextView tv = (TextView) v.findViewById(R.id.busStationName);
        tv.setText(mBusStations.get(position).getStationName());
        tv.setTag(position, mBusStations.get(position).getStationCode());
        System.out.println("this is the tag" + tv.getTag());
        return v;
    }
}
