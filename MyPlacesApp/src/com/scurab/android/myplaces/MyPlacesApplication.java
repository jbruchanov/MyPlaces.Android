package com.scurab.android.myplaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.scurab.android.myplaces.interfaces.OnLocationListener;
import com.scurab.android.myplaces.server.ServerConnection;
import com.scurab.android.myplaces.util.PropertyProvider;

public class MyPlacesApplication extends Application
{
	private ServerConnection mServer;
	private PropertyProvider mPropertyProvider;
	private LocationManager mLocationManager;

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
	
	public boolean isFineGeolocationEnabled()
	{
		if(mLocationManager == null)
			mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);		
		List<String> list = mLocationManager.getProviders(c, true);
		return list != null && list.size() > 0;
	}
	
	/**
	 * @param listener callback for gps best location found, can be null
	 * @return {@link LocationManager#getLastKnownLocation(String)} <br/>
	 * if {{@link #isFineGeolocationEnabled()} returns null or only GPS provider enabled, this returns null!
	 */	 
	public Location getMyLocation(final OnLocationListener listener)
	{
		Location result = null;
		if(isFineGeolocationEnabled())
		{
			Criteria c = new Criteria();
			final String provider = mLocationManager.getBestProvider(c, true);
			if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
				result = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			else if(mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
				result = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			if(listener != null)
			{
			LocationListener lh = new LocationListener()
			{
				@Override public void onStatusChanged(String provider, int status, Bundle extras){}
				@Override public void onProviderEnabled(String provider){}
				@Override public void onProviderDisabled(String provider){}
				@Override public void onLocationChanged(Location location)
				{
					listener.onLocationFound(provider, location);
				}
			};
			mLocationManager.requestSingleUpdate(c, lh,getMainLooper());
			}
			
		}
		return result;
	}
	
	public List<Address> getLocations(String query) throws IOException
	{
		Geocoder g = new Geocoder(this);
		return g.getFromLocationName(query, 5);
	}

	public List<Address> getLocation(double x, double y) throws IOException
	{
		double currentLatitude = y;
		double currentLongitude = x;
		Geocoder gcd = new Geocoder(this, Locale.getDefault());
		List<Address> addresses = gcd.getFromLocation(currentLatitude, currentLongitude, 3);
		if(addresses == null)
			addresses = new ArrayList<Address>();
		return addresses;
	}
	
	private LocationListener lh = new LocationListener()
	{
		@Override public void onStatusChanged(String provider, int status, Bundle extras){}
		@Override public void onProviderEnabled(String provider){}
		@Override public void onProviderDisabled(String provider){}
		@Override public void onLocationChanged(Location location){}
	};
}
