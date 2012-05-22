package com.scurab.android.myplaces.activity;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.R.layout;
import com.scurab.android.myplaces.presenter.MainActivityPresenter;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends MapActivity {
    /** Called when the activity is first created. */
	
	private MapView mMapView;
	private View mContentView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        new MainActivityPresenter(this);
    }
    
    private void init()
    {
    	setContentView(getMainContentView());
    	mMapView = (MapView)findViewById(R.id.mapView);
    }
    
    protected int getContentViewResId()
    {
    	return R.layout.main;
    }
    
    private View getMainContentView()
    {
    	if(mContentView == null)
    		mContentView = View.inflate(this, getContentViewResId(), null);
    	return mContentView;
    }

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	public MapView getMapView()
	{
		return mMapView;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity, menu);
	    return true;
	}
}