package com.scurab.android.myplaces.activity;

import com.google.android.maps.MapView;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.presenter.BasePresenter;
import com.scurab.android.myplaces.presenter.MainActivityPresenter15;
import com.scurab.android.myplaces.widget.MapItemPanel;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class MainActivity extends BaseMapActivity {
	
	private MapView mMapView;
	private View mContentView;
	private ImageButton mMyLocation;
	private ProgressBar mProgressBar;
	private MainActivityPresenter15 mPresenter;
	private MapItemPanel mMapItemPanel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        
       
    	mPresenter = getActivityPresenter();        
    }
    
    protected MainActivityPresenter15 getActivityPresenter()
    {
    	return new MainActivityPresenter15(this);
    }
    
    protected void init()
    {
    	setContentView(getContentView());
    	mMapView = (MapView)findViewById(R.id.mapView);
    	mMyLocation = (ImageButton)findViewById(R.id.ibMyLocation);
    	mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
    	mMapItemPanel = (MapItemPanel)findViewById(R.id.mapItemPanel);
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

	public ImageButton getMyLocationButton()
	{
		return mMyLocation;
	}

	public ProgressBar getProgressBar()
	{
		return mProgressBar;
	}

	@Override
	public BasePresenter getPresenter()
	{
		return mPresenter;
	}
	
	public MapItemPanel getMapItemPanel()
	{
		return mMapItemPanel;
	}
}