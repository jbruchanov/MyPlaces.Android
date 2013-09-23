package com.scurab.android.myplaces.interfaces;

import android.location.Location;

public interface OnLocationListener {
    public void onLocationFound(String provider, Location l);
}
