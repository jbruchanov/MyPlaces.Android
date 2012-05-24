package com.scurab.android.myplaces;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.test.AndroidTestCase;

public class MyPlacesApplicationTest extends AndroidTestCase
{

	public void testAppContext()
	{
		assertTrue(getContext().getApplicationContext() instanceof MyPlacesApplication);
	}
	public void testGetServerConnection()
	{
		MockMyPlacesApplication mpa = new MockMyPlacesApplication();
		mpa.attachBaseContext(getContext());
		assertNotNull(mpa.getServerConnection());
	}

	public void testGetPropertyProvider()
	{
		MockMyPlacesApplication mpa = new MockMyPlacesApplication();
		mpa.attachBaseContext(getContext());
		assertNotNull(mpa.getPropertyProvider());
	}
	
	/**
	 * Assumed that you have geolocation enabled!
	 */
	public void testIsLocationsEnabled()
	{
		MockMyPlacesApplication mpa = new MockMyPlacesApplication();
		mpa.attachBaseContext(getContext());
		assertTrue(mpa.isFineGeolocationEnabled());
	}
	
	public void testGetLocations() throws IOException
	{
		MockMyPlacesApplication mpa = new MockMyPlacesApplication();
		mpa.attachBaseContext(getContext());
		List<Address> result = mpa.getLocations("Prague");
		assertNotNull(result);
	}
	private class MockMyPlacesApplication extends MyPlacesApplication
	{
		@Override
		protected void attachBaseContext(Context base)
		{
			super.attachBaseContext(base);
		}
	}
	
	
}
