package com.scurab.android.myplaces.presenter;

import com.scurab.android.myplaces.MyPlacesApplication;
import com.scurab.android.myplaces.server.ServerConnection;
import com.scurab.android.myplaces.util.PropertyProvider;

import android.app.Activity;
import android.widget.Toast;

public abstract class BasePresenter
{
	Activity mContext;
	MyPlacesApplication mApplicationContext;
	ServerConnection mServer;
	
	public BasePresenter(Activity context)
	{
		mContext = context;
		mApplicationContext = (MyPlacesApplication) mContext.getApplicationContext();
	}
	
	public ServerConnection getServerConnection()
	{
		return mApplicationContext.getServerConnection();
	}
	
	public PropertyProvider getPropertyProvider()
	{
		return mApplicationContext.getPropertyProvider();
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
