package me.blueland.metro.activity;

import me.blueland.metro.R;
import me.blueland.metro.fragment.BusFragment;
import me.blueland.metro.fragment.CollectionFragment;
import me.blueland.metro.fragment.RailFragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost.OnTabChangeListener;

/**
 * Favourite Metro & Bus Stations
 * 
 * @author Te
 * 
 */
public class MainActivity extends FragmentActivity {

	Button blue;
	private FragmentTabHost mTabHost;
	View tabOne;
	View tabTwo;
	View tabThree;
	View tabFour;
	View tabFive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_activity);
		// find tabhost view in collection_activity;
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(),
				android.R.id.tabcontent);

		mTabHost.setBackground(getResources().getDrawable(R.color.white));

		mTabHost.addTab(mTabHost.newTabSpec("tab0").setIndicator(null, null),
				RailFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(null, null),
				BusFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(null, null),
				CollectionFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(null, null),
				RailFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator(null, null),
				RailFragment.class, null);
		tabOne = mTabHost.getTabWidget().getChildAt(0);
		tabTwo = mTabHost.getTabWidget().getChildAt(1);
		tabThree = mTabHost.getTabWidget().getChildAt(2);
		tabFour = mTabHost.getTabWidget().getChildAt(3);
		tabFive = mTabHost.getTabWidget().getChildAt(4);

		// refresh views to change background for each
		refreshTab();

		// setOnTabChangedListener to trigger background changes
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				System.out.println(tabId);
				switch (mTabHost.getCurrentTab()) {
				case 0:
					refreshTab();
					tabOne.setBackground(getResources().getDrawable(
							R.drawable.metro_clicked));
					break;
				case 1:
					refreshTab();
					tabTwo.setBackground(getResources().getDrawable(
							R.drawable.bus_clicked));
					break;
				case 2:
					refreshTab();
					tabThree.setBackground(getResources().getDrawable(
							R.drawable.favorite_clicked));
					break;
				case 3:
					refreshTab();
					tabFour.setBackground(getResources().getDrawable(
							R.drawable.nearby_clicked));
					break;
				case 4:
					refreshTab();
					tabFive.setBackground(getResources().getDrawable(
							R.drawable.map_clicked));
					break;
				default:
					break;
				}
			}
		});
		// Default setting, to collection fragment
		mTabHost.setCurrentTab(2);
	}


	private void refreshTab() {
		tabOne.setBackground(getResources().getDrawable(R.drawable.metro_un));
		tabTwo.setBackground(getResources().getDrawable(R.drawable.bus_un));
		tabThree.setBackground(getResources().getDrawable(
				R.drawable.favorite_un));
		tabFour.setBackground(getResources().getDrawable(R.drawable.nearby_un));
		tabFive.setBackground(getResources().getDrawable(R.drawable.map_un));
	}
}
