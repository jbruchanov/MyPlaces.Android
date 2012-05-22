package com.scurab.android.myplaces.presenter;

import java.util.HashMap;
import java.util.List;

import org.restlet.resource.Resource;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.activity.MainActivity;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.Star;
import com.scurab.android.myplaces.overlay.Overlays;
import com.scurab.android.myplaces.overlay.MyPlaceOverlayItem;

public class MainActivityPresenter extends BasePresenter
{
	private MainActivity mContext;
	private final static double COORD_HELP_MAPPER = 1E6;
	
	public MainActivityPresenter(MainActivity context)
	{
		super(context);
		mContext = context;
		init();
	}
	
	private void init()
	{
		onLoadingStars();
		onLoadingMapItems();
		
	}
	
	public void onLoadingStars()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				onLoadingStarsImpl();
			}
		},"onLoadingStars").start();
	}
	
	public void onLoadingMapItems()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				onLoadingMapItemsImpl();
			}
		},"onLoadingStars").start();
	}
	
	protected void onLoadingStarsImpl()
	{
		try
		{
			Star[] stars = getServerConnection().getStars();
			onLoadedStars(stars);
		}
		catch(Exception e)
		{
			showMessage(e);
		}
	}
	
	public void onLoadedStars(Star[] stars)
	{
		List<Overlay> mapOverlays = mContext.getMapView().getOverlays();
		Overlays<Star> itemizedoverlay = new Overlays<Star>(mContext,R.drawable.ico_star);
		for(Star s : stars)
		{
			int lat = (int)(s.getY() * COORD_HELP_MAPPER);
			int lng = (int)(s.getX() * COORD_HELP_MAPPER);
			MyPlaceOverlayItem<Star> oi = new MyPlaceOverlayItem<Star>(new GeoPoint(lat, lng), s);
			itemizedoverlay.addOverlay(oi);
		}
		mapOverlays.add(itemizedoverlay);
	}
	
	protected void onLoadingMapItemsImpl()
	{
		try
		{
			MapItem[] items = getServerConnection().getMapItems(14, 55, 16, 50);
			onLoadedMapItems(items);
		}
		catch(Exception e)
		{
			showMessage(e);
		}
	}
	
	public void onLoadedMapItems(MapItem[] items)
	{
		List<Overlay> mapOverlays = mContext.getMapView().getOverlays();
		Overlays<MapItem> itemizedoverlay = new Overlays<MapItem>(mContext,R.drawable.ic_launcher);
		Resources res = mContext.getResources();
		int i = 0;
		for(MapItem s : items)
		{
			int lat = (int)(s.getY() * COORD_HELP_MAPPER);
			int lng = (int)(s.getX() * COORD_HELP_MAPPER);
			MyPlaceOverlayItem<MapItem> oi = new MyPlaceOverlayItem<MapItem>(new GeoPoint(lat, lng), s);
			Drawable d = null;
			if(i++ % 2 == 0)
				d = res.getDrawable(R.drawable.ico_beer);			
			else
				d = res.getDrawable(R.drawable.ico_drink);			
			itemizedoverlay.addOverlay(oi,d);
		}
		mapOverlays.add(itemizedoverlay);
		
	}
}
