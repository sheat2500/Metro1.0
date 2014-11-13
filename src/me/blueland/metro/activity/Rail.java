package me.blueland.metro.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.blueland.metro.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Rail extends Activity {

	ListView listview;
	Button red, blue, green, silver, orange, yellow;
	SimpleAdapter SimpleAdapter;
	OnClickListener buttonOnclickListener;
	OnItemClickListener onItemClickListener;
	List<Map<String, Object>> listMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_rail);
		initListener();
		initView();
	}

	private void initListener() {
		// button 监听器
		buttonOnclickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int array_id = R.array.blue_line_station;
				int code_id = R.array.blue_line_station_code;
				switch (v.getId()) {
				case R.id.red_line:
					array_id = R.array.red_line_station;
					code_id = R.array.red_line_station_code;
					break;
				case R.id.blue_line:
					array_id = R.array.blue_line_station;
					code_id = R.array.blue_line_station_code;
					break;
				case R.id.green_line:
					array_id = R.array.green_line_station;
					code_id = R.array.green_line_station_code;
					break;
				case R.id.orange_line:
					array_id = R.array.orange_line_station;
					code_id = R.array.orange_line_station_code;
					break;
				case R.id.silver_line:
					array_id = R.array.silver_line_station;
					code_id = R.array.silver_line_station_code;
					break;
				case R.id.yellow_line:
					array_id = R.array.yellow_line_station;
					code_id = R.array.yellow_line_station_code;
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
				SimpleAdapter = new SimpleAdapter(getApplicationContext(),
						listMap, R.layout.rail_activity_item, new String[] {
								"array", "code" }, new int[] {
								R.id.stationName, R.id.stationCode });
				listview.setAdapter(SimpleAdapter);
			}
		};

		// 点击列表Item事件
		onItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) view.findViewById(R.id.stationCode);
				String stationCode = tv.getText().toString();
				Intent intent = new Intent(
						"me.blueland.metro.activity.RailStationPre");
				intent.putExtra("stationCode", stationCode);
				startActivity(intent);
			}

		};
	}

	protected void initView() {
		listview = (ListView) findViewById(R.id.showTrainStation);
		red = (Button) findViewById(R.id.red_line);
		blue = (Button) findViewById(R.id.blue_line);
		yellow = (Button) findViewById(R.id.yellow_line);
		orange = (Button) findViewById(R.id.orange_line);
		silver = (Button) findViewById(R.id.silver_line);
		green = (Button) findViewById(R.id.green_line);

		listview.setOnItemClickListener(onItemClickListener);
		red.setOnClickListener(buttonOnclickListener);
		blue.setOnClickListener(buttonOnclickListener);
		silver.setOnClickListener(buttonOnclickListener);
		green.setOnClickListener(buttonOnclickListener);
		yellow.setOnClickListener(buttonOnclickListener);
		orange.setOnClickListener(buttonOnclickListener);

		blue.callOnClick();

	}

}
