package com.scurab.android.myplaces.interfaces;

import com.google.android.gms.maps.model.LatLng;

public interface HasCoordinates {
    public double getX();

    public double getY();

    public LatLng getLatLng();
}
