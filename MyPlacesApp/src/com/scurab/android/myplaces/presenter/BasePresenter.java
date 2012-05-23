package com.scurab.android.myplaces.presenter;

import com.scurab.android.myplaces.MyPlacesApplication;
import com.scurab.android.myplaces.activity.MainActivity;
import com.scurab.android.myplaces.interfaces.OnLocationListener;
import com.scurab.android.myplaces.server.ServerConnection;
import com.scurab.android.myplaces.util.PropertyProvider;

import android.app.Activity;
import android.location.Location;
import android.widget.Toast;

public abstract class BasePresenter
{
	private Activity mContext;
	private MyPlacesApplication mApplication;
	
	public BasePresenter(Activity context)
	{
		mContext = context;		
		mApplication = (MyPlacesApplication) mContext.getApplication();
		if(mApplication  == null) //for testing purpose
			mApplication = (MyPlacesApplication) mContext.getApplicationContext();
	}
	
	public ServerConnection getServerConnection()
	{
		return mApplication.getServerConnection();
	}
	
	public PropertyProvider getPropertyProvider()
	{
		return mApplication.getPropertyProvider();
	}
	
	public boolean isFineGeolocationEnabled()
	{
		return mApplication.isFineGeolocationEnabled();
	}
	
	/**
	 * {@link MainActivity#getMyLocationButton()}
	 * 
	 */
	public Location getMyLocation(OnLocationListener listener)
	{
		return mApplication.getMyLocation(listener);
	}
	
	public void showMessage(int resId)
	{
		showMessage(mContext.getString(resId));
	}
	
	public void showMessage(int resId,Object... data)
	{
		showMessage(mContext.getString(resId, data));
	}
	
	public void showMessage(final String msg)
	{
		mContext.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public void showMessage(Throwable t)
	{
		showMessage(t.getMessage());
	}
	
}
