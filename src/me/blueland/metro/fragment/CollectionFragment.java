package me.blueland.metro.fragment;

import java.util.ArrayList;

import me.blueland.metro.R;
import me.blueland.metro.adapter.BusArrayAdapter;
import me.blueland.metro.adapter.RailArrayAdapter;
import me.blueland.metro.database.DBAdapter;
import me.blueland.metro.model.BusStation;
import me.blueland.metro.model.RailStation;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionFragment extends Fragment {

	DBAdapter dbadapter;
	RailArrayAdapter railAdapter;
	BusArrayAdapter busAdapter;
	ArrayList<RailStation> collection_rail;
	ArrayList<BusStation> collection_bus;
	ListView listBus;
	ListView listRail;
	Bundle bundle;

	OnItemClickListener RailonItemClickListener;
	OnItemClickListener BusonItemClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_collection, container,
				false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		dbadapter = new DBAdapter(getActivity());
		initView();
		init();
	}

	public void initView() {
		listBus = (ListView) getActivity().findViewById(R.id.showBusStations);
		listRail = (ListView) getActivity().findViewById(R.id.showRailStations);
		collection_rail = new ArrayList<RailStation>();
		collection_bus = new ArrayList<BusStation>();

		RailonItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String stationCode = ((TextView) view
						.findViewById(R.id.stationCode)).getText().toString();
				RailStation railstation = collection_rail.get(position);
				
				Intent intent = new Intent(
						"me.blueland.metro.activity.RailStationPre");
				intent.putExtra("intent", "CollectionFragment");
				intent.putExtra("line", railstation.getLine());
				intent.putExtra("stationCode", railstation.getStationCode());
				intent.putExtra("stationName", railstation.getStationName());
				intent.putExtra("latitude", railstation.getLatitude());
				intent.putExtra("longitude", railstation.getLongitude());
				startActivity(intent);
				
			}
		};
		
		BusonItemClickListener = new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
			
		};

	}

	public void init() {
		dbadapter.open();
		Cursor cursor = dbadapter.getAllFavourite();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				if (cursor.getString(cursor.getColumnIndex("rail")).equals("1")) {
					// 显示作用
					RailStation rail = new RailStation(
							cursor.getString(cursor.getColumnIndex("_id")),

							cursor.getString(cursor.getColumnIndex("line")),

							cursor.getString(cursor
									.getColumnIndex("stationCode")),
							cursor.getString(cursor
									.getColumnIndex("stationName")),
							cursor.getString(cursor.getColumnIndex("latitude")),
							cursor.getString(cursor.getColumnIndex("longitude")));
					collection_rail.add(rail);
				} else {
					BusStation bus = new BusStation(
							cursor.getString(cursor.getColumnIndex("id")),

							cursor.getString(cursor.getColumnIndex("line")),

							cursor.getString(cursor
									.getColumnIndex("stationCode")),
							cursor.getString(cursor
									.getColumnIndex("stationName")),
							cursor.getString(cursor.getColumnIndex("latitude")),
							cursor.getString(cursor.getColumnIndex("longitude")));
					collection_bus.add(bus);
				}
			}
			// Test
			railAdapter = new RailArrayAdapter(getActivity(),
					R.layout.fragment_collection_item, collection_rail);
			busAdapter = new BusArrayAdapter(getActivity(),
					R.layout.fragment_collection_item, collection_bus);
			listBus.setAdapter(busAdapter);
			listBus.setOnItemClickListener(BusonItemClickListener);
			listRail.setAdapter(railAdapter);
			listRail.setOnItemClickListener(RailonItemClickListener);
		}
		dbadapter.close();
	}
}
