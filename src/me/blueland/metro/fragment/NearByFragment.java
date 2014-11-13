package me.blueland.metro.fragment;

import me.blueland.metro.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class NearByFragment extends Fragment {

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater,
    		@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	View v = inflater.inflate(R.layout.fragment_nearby, container, false);
    	return v;
    }

    
    
}
