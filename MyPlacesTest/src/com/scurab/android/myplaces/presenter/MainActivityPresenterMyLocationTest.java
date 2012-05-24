package com.scurab.android.myplaces.presenter;

import android.app.Application;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;

import com.google.android.maps.GeoPoint;
import com.scurab.android.myplaces.MyPlacesApplication;
import com.scurab.android.myplaces.activity.MainActivity;
import com.scurab.android.myplaces.server.MockServerConnection;
import com.scurab.android.myplaces.server.ServerConnection;

public class MainActivityPresenterMyLocationTest extends ActivityUnitTestCase<MainActivity>
{
	public MainActivityPresenterMyLocationTest()
	{
		super(MainActivity.class);
	}
	
	@Override
	public void setApplication(Application application)
	{
		super.setApplication(new MockMyPlacesApplication());
	}
	public void testAddMyLocation()
	{
		startActivity(new Intent(), null, null);
		MainActivity ma = getActivity();
		MainActivityPresenter15 p = (MainActivityPresenter15) ma.getPresenter();		
		int c = p.getOverlayList().size();
		p.addMyLocationOverlay(0,0);
		assertEquals(c+1,p.getOverlayList().size());
	}
	
	public void testRemoveMyLocation()
	{
		startActivity(new Intent(), null, null);
		MainActivity ma = getActivity();
		MainActivityPresenter15 p = (MainActivityPresenter15) ma.getPresenter();		
		int c = p.getOverlayList().size();
		p.addMyLocationOverlay(0,0);
		assertEquals(c+1,p.getOverlayList().size());
		p.removeMyLocationOverlay();
		assertEquals(c,p.getOverlayList().size());
	}
	
	private class MockMyPlacesApplication extends MyPlacesApplication
	{
		@Override
		public ServerConnection getServerConnection()
		{
			return new MockServerConnection("");
		}
	}
}
