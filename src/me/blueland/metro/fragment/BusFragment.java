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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BusFragment extends Fragment {

	ListView busStationList;
	SeekBar seekBar;
	EditText editText;
	SimpleAdapter simpleAdapter;
	List<Map<String, Object>> listMap;
	OnItemClickListener onItemClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_bus, container, false);
		busStationList = (ListView) v.findViewById(R.id.busListView);
		// seekBar = (SeekBar) v.findViewById(R.id.seekBar);
		editText = (EditText) v.findViewById(R.id.editText);
		listMap = new ArrayList<Map<String, Object>>();
		String[] busStationName = getResources().getStringArray(
				R.array.bus_station_name);
		String[] busStationCode = getResources().getStringArray(
				R.array.bus_station_code);
		for (int i = 0; i < busStationName.length; i++) {
			Map<String, Object> listitem = new HashMap<String, Object>();
			listitem.put("busStationName", busStationName[i]);
			listitem.put("busStationCode", busStationCode[i]);
			listMap.add(listitem);
		}

		simpleAdapter = new SimpleAdapter(getActivity(), listMap,
				R.layout.fragment_bus_item, new String[] { "busStationName",
						"busStationCode" }, new int[] { R.id.busStationName,
						R.id.busStationCode });
		busStationList.setAdapter(simpleAdapter);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initListener();
		busStationList.setOnItemClickListener(onItemClickListener);
	}

	public void initListener() {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				simpleAdapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		onItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						"me.blueland.metro.activity.BusStationPre");
				String busStationCode = ((TextView) view
						.findViewById(R.id.busStationCode)).getText()
						.toString();
				System.out.println(busStationCode);
				// Tell the activity from BusFragment
				intent.putExtra("intent", "BusFragment");
				intent.putExtra("busStationCode", busStationCode);
				startActivity(intent);
			}
		};

	}
}
