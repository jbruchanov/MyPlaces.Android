package com.scurab.android.myplaces.overlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MyPlaceOverlayItem<T> extends OverlayItem
{
	private T mObject;
	public MyPlaceOverlayItem(GeoPoint arg0, T object)
	{
		super(arg0, "arg1", "arg2");
		mObject = object;
		
	}
	
	public T getObject()
	{
		return mObject;		
	}
	
}
