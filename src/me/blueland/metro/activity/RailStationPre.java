package me.blueland.metro.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.blueland.metro.R;
import me.blueland.metro.database.DBAdapter;
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

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 
 * @author Te
 * 
 */

public class RailStationPre extends Activity {

	private Menu menu;
	private ListView listView;
	private ProgressDialog progressDialog;
	private Button button;
	private ActionBar actionBar;
	private List<RailStationPrediction> railstationpredictions;
	private String stationCode;
	private String stationName;
	private String line;
	private int latArray;
	private int lonArray;
	private String lat;
	private String lon;
	private Intent intent;
	DBAdapter adapter = new DBAdapter(this);

	// store array_position in stationCode for getting related latitude and
	// longtitude in array
	private int array_position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rail_station_pre_activity);
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		intent = getIntent();
		if (intent.getStringExtra("intent").equals("RailFragment")) {
			initViewFromRailFragment();
		} else {
			initViewFromCollectionFragment();
		}
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.menu = menu;
		MenuInflater menuinflater = new MenuInflater(this);
		menuinflater.inflate(R.menu.menu_activity_railstationpre, menu);

		// check the database whether has some record if have some, update the
		// UI
		adapter.open();
		Cursor cursor = adapter.queryFavourate(stationCode);
		if (cursor.getCount() != 0) {
			// same value in the table, so change the menu item
			MenuItem menuitem = menu.findItem(R.id.addToCollection);
			menuitem.setEnabled(false);
			adapter.close();
		} else {
			adapter.close();
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			// Click the back button, then back to previous activity
			finish();
			break;
		case R.id.addToCollection:
			System.out.println("2");
			break;
		case R.id.refreshList:
			System.out.println("3");
			break;
		case R.id.showMapPath:
			System.out.println("4");
			break;
		default:
			break;
		}
		return true;
	}

	// Circle: Listener - button onClick event - callback function
	public boolean addToCollection(MenuItem item) {
		adapter.open();
		adapter.insertFavourate(line, stationName, stationCode, lat, lon, "1");
		item.setEnabled(false);
		adapter.close();
		return true;
	}

	// menu onclick event should be like below. Different from button events
	public boolean refreshList(MenuItem item) {
		new RailController().execute(stationCode);
		// click event: first trigger Listenerning, if return false, transfer to
		// this method
		// if return true, stop conducting
		return true;
	}

	public boolean showMapPath(MenuItem item) {
		System.out.println("before back");
		return true;
	}

	// 逻辑未分离
	public void init() {
		progressDialog.show();
		new RailController().execute(stationCode);
	}

	// Initialize all views and get data from previous for further use
	public void initViewFromRailFragment() {

		// Get data from previous fragment for the collection

		stationCode = intent.getStringExtra("stationCode");
		stationName = intent.getStringExtra("stationName");
		// Because the line has been transfered to this activity, so the station
		// prediction list could be optimized
		line = intent.getStringExtra("line");
		// default value = -1
		array_position = intent.getIntExtra("position", -1);
		// 取出经纬度
		latArray = intent.getIntExtra("latitude", -1);
		lonArray = intent.getIntExtra("longitude", -1);
		lat = getResources().getStringArray(latArray)[array_position];
		lon = getResources().getStringArray(lonArray)[array_position];

		// latitude = getResources().getStringArray(R.array.)
		listView = (ListView) findViewById(R.id.showTrainStation);
		// change addToCollection Button state of clickable
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading At least Faster Than Trains!");
	}

	// clicked through listitem from collection fragment.
	public void initViewFromCollectionFragment() {
		stationCode = intent.getStringExtra("stationCode");
		line = intent.getStringExtra("line");
		stationCode = intent.getStringExtra("stationCode");
		stationName = intent.getStringExtra("stationName");
		lat = intent.getStringExtra("latitude");
		lon = intent.getStringExtra("longitude");
		listView = (ListView) findViewById(R.id.showTrainStation);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading At least Faster Than Trains!");
	}

	// RailController
	protected class RailController extends
			AsyncTask<String, Integer, List<RailStationPrediction>> {

		// extends
		@Override
		protected List<RailStationPrediction> doInBackground(
				String... stationCode) {

			System.out.println(lat + "," + lon);

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
			// progressDialog dismiss in the onPostExecute method.
			// if refreshing the list, do not have to dismiss the dialog
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
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
		 * case 1: RailStationPrediction; case 2:
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
