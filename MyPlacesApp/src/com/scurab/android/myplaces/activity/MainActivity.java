package com.scurab.android.myplaces.activity;

import com.google.android.maps.MapView;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.presenter.MainActivityPresenter;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseMapActivity {
	
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
    	setContentView(getContentView());
    	mMapView = (MapView)findViewById(R.id.mapView);
    }
    
    protected int getContentViewResId()
    {
    	return R.layout.main;
    }
    
    @Override
	public View getContentView()
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

}