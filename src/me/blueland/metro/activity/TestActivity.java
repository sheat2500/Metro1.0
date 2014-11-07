package me.blueland.metro.activity;

import me.blueland.metro.R;
import me.blueland.metro.controller.RailController;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class TestActivity extends Activity {
	private Context context;
	private ListView listView;
	private ProgressDialog progressDialog;
	private Button button;
	private ListView listView2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		System.out.println("oncreate");
		// 初始化View
		initView();
		
		Log.i("Create", "=====");
	}

	protected void onPause() {
		super.onPause();
		Log.i("onPause", "=====");
	}

	protected void onResume() {

		super.onResume();
		Log.i("onResume", "=====");
	}

	protected void onDestroy() {

		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());

	}

	public void init() {
		// 注册活动
		// progressDialog = new ProgressDialog(this);
		// progressDialog.setMessage("正在获取数据");
		// progressDialog.show();
	}

	public void initView() {
		listView = (ListView) findViewById(R.id.showTrainStation);
		// listView2 = (ListView) findViewById(R.id.listView2);
		button = (Button) findViewById(R.id.red_button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("button");
				init();
			}
		});

	}
}
