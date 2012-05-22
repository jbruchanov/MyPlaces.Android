package com.scurab.android.myplaces;

import android.app.Application;

import com.scurab.android.myplaces.server.ServerConnection;
import com.scurab.android.myplaces.util.PropertyProvider;

public class MyPlacesApplication extends Application
{
	private ServerConnection mServer;
	private PropertyProvider mPropertyProvider;	

	public ServerConnection getServerConnection()
	{
		if(mServer == null)
			mServer = new ServerConnection(getPropertyProvider().getString(R.string.PROPERTY_SERVER_URL, M.Defaults.PROPERTY_SERVER_URL));
		
		return mServer;
	}
	
	public PropertyProvider getPropertyProvider()
	{
		if(mPropertyProvider == null)
			mPropertyProvider = PropertyProvider.getProvider(this);
		
		return mPropertyProvider;
	}
}
