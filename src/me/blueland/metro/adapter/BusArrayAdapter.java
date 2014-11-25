package me.blueland.metro.adapter;

import java.util.ArrayList;
import java.util.List;

import me.blueland.metro.R;
import me.blueland.metro.model.BusStation;
import me.blueland.metro.model.RailStation;
import android.content.Context;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BusArrayAdapter extends ArrayAdapter<BusStation> {

	Context context;
	int resource;
	ArrayList<BusStation> arrayList;
	// Code as a tag in TextView
	String showStationCode;
	TextView showStationName;

	public BusArrayAdapter(Context context, int resource,
			ArrayList<BusStation> arraylist) {
		super(context, resource, arraylist);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resource = resource;
		this.arrayList = arraylist;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View v = layoutInflater.inflate(resource, null);
		showStationName = (TextView) v.findViewById(R.id.stationName);
//		showStationName.setText();
//		showStationName.setTag(arrayStationCode[position]);
		return v;
	}
}
