package com.scurab.android.myplaces;

import android.content.Context;
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
	
	private class MockMyPlacesApplication extends MyPlacesApplication
	{
		@Override
		protected void attachBaseContext(Context base)
		{
			super.attachBaseContext(base);
		}
	}
}
