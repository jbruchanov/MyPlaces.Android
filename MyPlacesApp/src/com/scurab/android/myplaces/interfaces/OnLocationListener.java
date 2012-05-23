package com.scurab.android.myplaces.interfaces;

import android.location.Location;
import android.location.LocationListener;

public interface OnLocationListener
{
	public void onLocationFound(String provider, Location l);
}
