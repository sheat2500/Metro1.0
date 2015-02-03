package me.blueland.metro.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost.OnTabChangeListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

import me.blueland.metro.R;
import me.blueland.metro.fragment.BusFragment;
import me.blueland.metro.fragment.CollectionFragment;
import me.blueland.metro.fragment.MapFragment;
import me.blueland.metro.fragment.NearByFragment;
import me.blueland.metro.fragment.RailFragment;

/**
 * Favourite Metro & Bus Stations
 *
 * @author Te
 */
public class MainActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private FragmentTabHost mTabHost;
    View tabOne;
    View tabTwo;
    View tabThree;
    View tabFour;
    View tabFive;

    GoogleApiClient googleApiClient;
    boolean mRequestingLocationUpdates = true;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    String mLastUpdateTime;
    String lat;
    String lon;


    final String REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY";
    final String LOCATION_KEY = "LOCATION_KEY";
    final String LAST_UPDATED_TIME_STRING_KEY = "LAST_UPDATED_TIME_STRING_KEY";


    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        //
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        // Always has null value, coz it needs time to acquire, so have to use the callback method
        if (mCurrentLocation != null) {
            System.out.println(String.valueOf(mCurrentLocation.getLatitude()));
            System.out.println(String.valueOf(mCurrentLocation.getLongitude()));
        }
        System.out.println("Error");

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        lat = String.valueOf(mCurrentLocation.getLatitude());
        lon = String.valueOf(mCurrentLocation.getLongitude());
        System.out.println(String.valueOf(mCurrentLocation.getLatitude()) + " ,  " + String.valueOf(mCurrentLocation.getLongitude()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);

        buildGoogleApiClient();

        createLocationRequest();

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
                NearByFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator(null, null),
                MapFragment.class, null);
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

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Stop LocationUpdates
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    private void refreshTab() {
        tabOne.setBackground(getResources().getDrawable(R.drawable.metro_un));
        tabTwo.setBackground(getResources().getDrawable(R.drawable.bus_un));
        System.out.println(tabOne.getWidth() + ";" + tabOne.getHeight());
        tabThree.setBackground(getResources().getDrawable(
                R.drawable.favorite_un));
        tabFour.setBackground(getResources().getDrawable(R.drawable.nearby_un));
        tabFive.setBackground(getResources().getDrawable(R.drawable.map_un));
    }

    // Method for NearByFragment to get Current Location;
    public Location getmCurrentLocation() {
        return mCurrentLocation;
    }

}
