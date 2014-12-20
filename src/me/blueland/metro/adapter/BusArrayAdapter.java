package me.blueland.metro.adapter;

import java.util.ArrayList;

import me.blueland.metro.R;
import me.blueland.metro.model.BusStation;
import me.blueland.metro.model.RailStation;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BusArrayAdapter extends ArrayAdapter<BusStation> {

	Context context;
	int resource;
	ArrayList<BusStation> list;
	TextView showStationCode;
	TextView showStationName;

	public BusArrayAdapter(Context context, int resource,
			ArrayList<BusStation> list) {
		super(context, resource, list);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resource = resource;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View v = layoutInflater.inflate(resource, null);
		showStationCode = (TextView) v.findViewById(R.id.stationCode);
		showStationName = (TextView) v.findViewById(R.id.stationName);
		showStationCode.setText(list.get(position).getStationCode());
		showStationName.setText(list.get(position).getStationName());
		return v;
	}
}
