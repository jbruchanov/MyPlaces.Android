package com.scurab.android.myplaces.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.server.MockServerConnection;
import com.scurab.android.myplaces.server.ServerConnection;

import android.content.Context;
import android.test.AndroidTestCase;
import android.widget.ImageButton;

public class MainActivityPresenterTest extends AndroidTestCase
{
	
	public void testOnAddStarClick()
	{
		MockMainActivityPresenter  mma = new MockMainActivityPresenter();
		assertEquals(MainActivityPresenter15.STATE_DEFAULT,mma.getState());
		mma.onAddMenuItemClick(R.id.muAddStar);
		assertEquals(MainActivityPresenter15.STATE_ADDING_NEW_STAR,mma.getState());
		//click to map should return state to default
		mma.onMapClick(new LatLng(0, 0));
		assertEquals(MainActivityPresenter15.STATE_DEFAULT,mma.getState());
	}

	public void testOnAddMapItemClick()
	{
		MockMainActivityPresenter  mma = new MockMainActivityPresenter();
		assertEquals(MainActivityPresenter15.STATE_DEFAULT,mma.getState());
		mma.onAddMenuItemClick(R.id.muAddMapItem);
		assertEquals(MainActivityPresenter15.STATE_ADDING_NEW_ITEM,mma.getState());
		//click to map should return state to default
		mma.onMapClick(new LatLng(0, 0));
		assertEquals(MainActivityPresenter15.STATE_DEFAULT,mma.getState());
	}
	
	
	private class MockMainActivityPresenter extends MainActivityPresenter15
	{
		public MockMainActivityPresenter()
		{
			super(new MockMainActivity(getContext()));
		}
		
		@Override
		public ServerConnection getServerConnection()
		{
			return new MockServerConnection("");
		}
		
		@Override
		protected void showSmileyDialog(LatLng location)
		{
			onAddNewStar(location,0);
		}
	}
	
	private class MockMainActivity extends com.scurab.android.myplaces.activity.MainActivity
	{
		public MockMainActivity(Context c)
		{
			attachBaseContext(c);
		}
		
		@Override
		public ImageButton getMyLocationButton()
		{
			return new ImageButton(mContext);
		}
	}
}
