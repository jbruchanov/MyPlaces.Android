package com.scurab.android.myplaces.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.Overlay;
import com.scurab.android.myplaces.activity.MainActivity;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.Star;
import com.scurab.android.myplaces.server.MockServerConnection;
import com.scurab.android.myplaces.server.ServerConnection;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;
import android.widget.ImageButton;


public class MainActivityPresenterLoadingTest extends AndroidTestCase
{
	private volatile MockMainActivityPresenter mmap;
	private volatile MockServerConnection msc;
	boolean checkedStars = false;
	boolean checkedMapItems = false;
	boolean checkedLoadedItemsToMap = false;
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		msc = new MockServerConnection("");
		checkedStars = false;
		checkedMapItems = false;
		checkedLoadedItemsToMap = false;
	}

	public void testInitLoadStars() throws InterruptedException
	{
		mmap = new MockMainActivityPresenter(new MockMainActivity(mContext));		
		mmap.stars = true;
		mmap.onLoadingStarsImpl();
		msc.DOWNLOAD_COUNT = 1;
		assertTrue(checkedStars);			
	}

	public void testInitLoadMapItems() throws InterruptedException
	{
		mmap = new MockMainActivityPresenter(new MockMainActivity(mContext));
		mmap.mapitems = true;
		mmap.onLoadingMapItemsImpl();
		msc.DOWNLOAD_COUNT = 1;
		assertTrue(checkedMapItems);			
	}
	
	public void testLoadToListOverlaysStars()
	{
		mmap = new MockMainActivityPresenter(new MockMainActivity(mContext));
		mmap.stars = true;
		mmap.onLoadingStarsImpl();
		msc.DOWNLOAD_COUNT = 1;
		assertTrue(checkedLoadedItemsToMap);
		assertEquals(msc.DOWNLOAD_COUNT, mmap.getOverlayList().size());
	}
	
	public void testLoadToListOverlaysMapItems()
	{
		mmap = new MockMainActivityPresenter(new MockMainActivity(mContext));
		mmap.mapitems = true;
		mmap.onLoadingMapItemsImpl();		
		msc.DOWNLOAD_COUNT = 1;
		assertTrue(checkedLoadedItemsToMap);
		assertEquals(msc.DOWNLOAD_COUNT, mmap.getOverlayList().size());
	}
	
	private class MockMainActivityPresenter extends MainActivityPresenter15
	{
		List<Overlay> overlays;
		boolean stars = false;
		boolean mapitems = false;
		public MockMainActivityPresenter(MainActivity context)
		{
			super(context);			
		}
		
		@Override
		public ServerConnection getServerConnection()
		{
			return msc;
		}		
		
		@Override
		protected void loadData()
		{
			//dont load by deafult
		}
		
		@Override
		public void onLoadedStars(Star[] stars)
		{
			
			assertNotNull(stars);
			if(stars.length == 0)
			{
				Star s = new Star();
				s.setX(5);s.setY(5);
				stars = new Star[] {s};
			}			
			super.onLoadedStars(stars);
			checkedStars = true;
			checkedLoadedItemsToMap = true;
		}
		
		@Override
		public void onLoadedMapItems(MapItem[] items) 
		{
			if(!mapitems) return;
			assertNotNull(items);		
			if(items.length == 0)
			{
				MapItem s = new MapItem();
				s.setX(5);s.setY(5);
				items = new MapItem[] {s};
			}
			super.onLoadedMapItems(items);
			checkedMapItems = true;
			checkedLoadedItemsToMap = true;
		};
		
		@Override
		protected List<Overlay> getOverlayList()
		{
			if(overlays == null)
				overlays = new ArrayList<Overlay>();
			return overlays;
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
