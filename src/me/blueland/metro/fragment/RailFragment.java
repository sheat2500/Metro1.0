package me.blueland.metro.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.blueland.metro.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RailFragment extends Fragment {

	ListView listview;
	Button red, blue, green, silver, orange, yellow;
	SimpleAdapter SimpleAdapter;
	OnClickListener buttonOnclickListener;
	OnItemClickListener onItemClickListener;
	List<Map<String, Object>> listMap;
	// get the clicked button showing which line for the next activity
	int array_id = R.array.blue_line_station;
	int code_id = R.array.blue_line_station_code;
	int latitude_id = R.array.blue_line_station_Latitude;
	int longitude_id = R.array.blue_line_station_Latitude;
	String line = "BLUE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_rail, container, false);

		initListener();

		listview = (ListView) v.findViewById(R.id.showTrainStation);
		red = (Button) v.findViewById(R.id.red_line);
		blue = (Button) v.findViewById(R.id.blue_line);
		yellow = (Button) v.findViewById(R.id.yellow_line);
		orange = (Button) v.findViewById(R.id.orange_line);
		silver = (Button) v.findViewById(R.id.silver_line);
		green = (Button) v.findViewById(R.id.green_line);

		listview.setOnItemClickListener(onItemClickListener);
		red.setOnClickListener(buttonOnclickListener);
		blue.setOnClickListener(buttonOnclickListener);
		silver.setOnClickListener(buttonOnclickListener);
		green.setOnClickListener(buttonOnclickListener);
		yellow.setOnClickListener(buttonOnclickListener);
		orange.setOnClickListener(buttonOnclickListener);
		return v;
	}

	// Conduct between onCreateView and onCreateView
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		blue.callOnClick();
	}

	private void initListener() {
		// button 监听器
		buttonOnclickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.red_line:
					line = "RED";
					array_id = R.array.red_line_station;
					code_id = R.array.red_line_station_code;
					latitude_id = R.array.red_line_station_Latitude;
					longitude_id = R.array.red_line_station_longitude;
					break;
				case R.id.blue_line:
					line = "BLUE";
					array_id = R.array.blue_line_station;
					code_id = R.array.blue_line_station_code;
					latitude_id = R.array.blue_line_station_Latitude;
					longitude_id = R.array.blue_line_station_longitude;
					break;
				case R.id.green_line:
					line = "GREEN";
					array_id = R.array.green_line_station;
					code_id = R.array.green_line_station_code;
					latitude_id = R.array.green_line_station_Latitude;
					longitude_id = R.array.green_line_station_longitude;
					break;
				case R.id.orange_line:
					line = "ORANGE";
					array_id = R.array.orange_line_station;
					code_id = R.array.orange_line_station_code;
					latitude_id = R.array.orange_line_station_Latitude;
					longitude_id = R.array.orange_line_station_longitude;
					break;
				case R.id.silver_line:
					line = "SILVER";
					array_id = R.array.silver_line_station;
					code_id = R.array.silver_line_station_code;
					latitude_id = R.array.silver_line_station_Latitude;
					longitude_id = R.array.silver_line_station_longitude;
					break;
				case R.id.yellow_line:
					line = "YELLOW";
					array_id = R.array.yellow_line_station;
					code_id = R.array.yellow_line_station_code;
					latitude_id = R.array.yellow_line_station_Latitude;
					longitude_id = R.array.yellow_line_station_longitude;
					break;
				default:
					break;
				}
				// 渲染列表

				String[] array = getResources().getStringArray(array_id);
				String[] code = getResources().getStringArray(code_id);
				listMap = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < array.length; i++) {
					Map<String, Object> listitem = new HashMap<String, Object>();
					listitem.put("array", array[i]);
					listitem.put("code", code[i]);
					listMap.add(listitem);
				}
				// You can use getActivity(), which returns the activity
				// associated with a fragment.
				// The activity is a context (since Activity extends Context).

				SimpleAdapter = new SimpleAdapter(getActivity(), listMap,
						R.layout.fragment_rail_item, new String[] { "array",
								"code" }, new int[] { R.id.stationName,
								R.id.stationCode });
				listview.setAdapter(SimpleAdapter);
			}
		};

		// 点击列表Item事件
		onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String stationCode = ((TextView) view
						.findViewById(R.id.stationCode)).getText().toString();
				String stationName = ((TextView) view
						.findViewById(R.id.stationName)).getText().toString();
				Intent intent = new Intent(
						"me.blueland.metro.activity.RailStationPre");
				// transfer 5 parameters for the next activity
				// postion for getting specific latitude and longitude in the
				// next activity
				intent.putExtra("intent", "RailFragment");
				intent.putExtra("line", line);
				intent.putExtra("position", position);
				intent.putExtra("stationCode", stationCode);
				intent.putExtra("stationName", stationName);
				intent.putExtra("latitude", latitude_id);
				intent.putExtra("longitude", longitude_id);
				startActivity(intent);
			}
		};
	}
}
