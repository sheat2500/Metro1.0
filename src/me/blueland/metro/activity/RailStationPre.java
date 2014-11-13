package me.blueland.metro.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.blueland.metro.R;
import me.blueland.metro.model.RailStationPrediction;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RailStationPre extends Activity {

	private ListView listView;
	private ProgressDialog progressDialog;
	private Button button;
	private String stationCode;
	List<RailStationPrediction> railstationpredictions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rail_station_pre_activity);
		System.out.println("oncreate");
		initView();
		init();
	}

	// 逻辑未分离
	public void init() {
		progressDialog.show();
		new RailController().execute(stationCode);
		progressDialog.cancel();
		// Image[] IMAGEs = new Image[railstationpredictions.size()];
	}

	public void initView() {
		stationCode = getIntent().getStringExtra("stationCode");
		listView = (ListView) findViewById(R.id.showTrainStation);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在获取数据");
	}

	// RailController
	protected class RailController extends
			AsyncTask<String, Integer, List<RailStationPrediction>> {

		// extends
		@Override
		protected List<RailStationPrediction> doInBackground(
				String... stationCode) {

			HttpClient httpclient;
			// 请求url
			String url;
			url = "http://api.wmata.com/StationPrediction.svc/json/GetPrediction/"
					+ stationCode[0] + "?api_key=kfgpmgvfgacx98de9q3xazww";

			// 标示代表 RailStationPrediction
			int index = 1;
			HttpGet httpGet = new HttpGet(url);
			httpclient = new DefaultHttpClient();
			StringBuilder result = new StringBuilder();

			try {
				HttpResponse httpResponse = httpclient.execute(httpGet);
				HttpEntity httpentity = httpResponse.getEntity();
				if (httpentity != null) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(httpentity.getContent()));
					String line = null;
					while ((line = br.readLine()) != null) {
						result.append(line);
					}
					// 数据解析
					railstationpredictions = parseJSON(result.toString(), index);
					// 可以更改的地方，是否可以使用服务对数据进行渲染 handleMessage();
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return railstationpredictions;
		}

		@Override
		protected void onPostExecute(
				List<RailStationPrediction> railstationpredictions) {
			// TODO Auto-generated method stub
			// List<RailStationPrediction> railstationpredictions = null;
			List<Map<String, Object>> listMap;
			RailStationPrediction railstationprediction;

			listMap = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < railstationpredictions.size(); i++) {
				if ((railstationprediction = railstationpredictions.get(i)) != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					int image = drawableSelection(railstationprediction
							.getLINE().toString());
					map.put("img", image);
					map.put("lines", railstationprediction.getLINE().toString());
					map.put("mins", railstationprediction.getMIN().toString());
					map.put("destinationame", railstationprediction
							.getDESTINATIONNAME().toString());
					listMap.add(map);
				}
			}

			SimpleAdapter simpleAdapter = new SimpleAdapter(getApplication(),
					listMap, R.layout.rail_station_pre_activity_item,
					new String[] { "img", "lines", "mins", "destinationame" },
					new int[] { R.id.color_line, R.id.lineName,
							R.id.predictionMin, R.id.destinationName });
			listView.setAdapter(simpleAdapter);
			super.onPostExecute(railstationpredictions);
		}
	}

	private int drawableSelection(String color) {
		if (color.equals("SV")) {
			return R.drawable.silver;
		} else if (color.equals("GR")) {
			return R.drawable.green;
		} else if (color.equals("RD")) {
			return R.drawable.red;
		} else if (color.equals("YL")) {
			return R.drawable.yellow;
		} else if (color.equals("OR")) {
			return R.drawable.orange;
		} else
			return R.drawable.blue;
	}

	// 解析JSON,并且封装
	private List<RailStationPrediction> parseJSON(String result, int value) {
		/**
		 * case 1: RailStationPrediction case 2:
		 */
		switch (value) {
		case 1:

			// Model: railStationPrediction
			RailStationPrediction railStationPrediction;
			try {

				List<RailStationPrediction> railstationpredictions = new ArrayList<RailStationPrediction>();
				JSONObject jsonobject = new JSONObject(result);
				JSONArray jsonarray = jsonobject.getJSONArray("Trains");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonObject = jsonarray.getJSONObject(i);
					railStationPrediction = new RailStationPrediction(
							jsonObject.getString("Line"),
							jsonObject.getString("Min"),
							jsonObject.getString("Destination"),
							jsonObject.getString("DestinationName"));
					railstationpredictions.add(railStationPrediction);
				}
				return railstationpredictions;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Add more cases, if having more url requests

		default:
			break;
		}
		return null;
	}
}
