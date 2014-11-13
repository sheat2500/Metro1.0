package me.blueland.metro.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import android.os.AsyncTask;

public class RailController extends
		AsyncTask<String, Integer, List<RailStationPrediction>> {

	HttpClient httpclient;
	String url;
	List<RailStationPrediction> railstationpredictions;

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

	// extends
	@Override
	protected List<RailStationPrediction> doInBackground(String... stationCode) {
		// 请求url
		url = "http://api.wmata.com/StationPrediction.svc/json/GetPrediction/"
				+ stationCode + "?api_key=kfgpmgvfgacx98de9q3xazww";

		// 标示代表 RailStationPrediction
		int index = 1;
		HttpGet httpGet = new HttpGet(url);
		httpclient = new DefaultHttpClient();
		StringBuilder result = new StringBuilder();

		try {
			HttpResponse httpResponse = httpclient.execute(httpGet);
			HttpEntity httpentity = httpResponse.getEntity();
			if (httpentity != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						httpentity.getContent()));
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
	protected void onPostExecute(List<RailStationPrediction> result) {
		// TODO Auto-generated method stub
		List<RailStationPrediction> railstationpredictions = null;
		List<Map<String, Object>> listMap;
		Map<String, Object> map;
		RailStationPrediction railstationprediction;
		
		String[] LINEs = new String[railstationpredictions.size()];
		String[] MINs = new String[railstationpredictions.size()];
		String[] DESTINATIONNAMEs = new String[railstationpredictions.size()];

		for (int i = 0; i < railstationpredictions.size(); i++) {
			if ((railstationprediction = railstationpredictions.get(i)) != null) {
				LINEs[i] = railstationprediction.getLINE();
				MINs[i] = railstationprediction.getMIN();
				DESTINATIONNAMEs[i] = railstationprediction
						.getDESTINATIONNAME();
			}
		}
//		map = new HashMap();
//		map.put("lines", LINEs);
//		map.put("mins", MINs);
//		map.put("destinationame", DESTINATIONNAMEs);
//		listMap = new ArrayList<Map<String, Object>>();
//		listMap.add(map);
//		SimpleAdapter simpleAdapter = new SimpleAdapter(getApplication(),
//				listMap, R.layout.rail_station_pre_activity_item, new String[] {
//						"lines", "mins", "destinationame" },
//				new int[] { R.id.lineName, R.id.predictionMin,
//						R.id.destinationName });
//		listView.setAdapter(simpleAdapter);
//		progressDialog.cancel();
//		super.onPostExecute(result);
	}
}
