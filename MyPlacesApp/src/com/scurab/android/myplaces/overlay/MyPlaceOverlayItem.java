package com.scurab.android.myplaces.overlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.scurab.android.myplaces.M;
import com.scurab.android.myplaces.datamodel.MapElement;

public class MyPlaceOverlayItem extends OverlayItem
{
	public MyPlaceOverlayItem(MapElement t)
	{
		super(new GeoPoint((int)(t.getY() * M.COORD_HELP_MAPPER), (int)(t.getX() * M.COORD_HELP_MAPPER)), "arg1", "arg2");
	}
}
