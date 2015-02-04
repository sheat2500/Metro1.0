package me.blueland.metro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.blueland.metro.MetroApplication;
import me.blueland.metro.R;
import me.blueland.metro.model.BusRoute;

public class BusFragment extends Fragment implements SearchView.OnQueryTextListener {

    ListView busStationList;
    SearchView searchView;
    ArrayList<Map<String, String>> mRouteArrayList;
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

        searchView = (SearchView) v.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Bus Station by Name");

        ArrayList<BusRoute> mBusRoutes = MetroApplication.getInstance().getmArrayListBusStops();
        mRouteArrayList = new ArrayList<>();
        for (int i = 0; i < mBusRoutes.size(); i++) {
            Map<String, String> busRoute = new HashMap<>();
            busRoute.put("routeId", mBusRoutes.get(i).getRouteId());
            busRoute.put("routeName", mBusRoutes.get(i).getRouteName());
            mRouteArrayList.add(busRoute);
        }
        String[] name = {"routeId", "routeName"};
        int[] id = {R.id.busRouteID, R.id.busRouteName};
        SimpleAdapter mBusRouteAdapter = new SimpleAdapter(getActivity(), mRouteArrayList, R.layout.fragment_bus_item, name, id);
        busStationList.setAdapter(mBusRouteAdapter);
        busStationList.setTextFilterEnabled(true);
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

        onItemClickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub


                Intent intent = new Intent(
                        "me.blueland.metro.activity.BusStationPre");
                String busRouteID = ((TextView) view.findViewById(R.id.busRouteName)).getText().toString();
                String busRouteName = ((TextView) view.findViewById(R.id.busRouteID)).getText().toString();
                BusRoute busRoute = new BusRoute(busRouteID, busRouteName);
                intent.putExtra("intent", "BusFragment");
                intent.putExtra("busRoute", busRoute);
                startActivity(intent);
            }
        };
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            busStationList.clearTextFilter();
        } else {
            busStationList.setFilterText(newText);
            searchView.requestFocus();
        }
        return true;
    }
}
