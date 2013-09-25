package com.scurab.android.myplaces.datamodel;

import com.google.android.gms.maps.model.LatLng;
import com.scurab.android.myplaces.interfaces.HasCoordinates;
import com.scurab.android.myplaces.interfaces.HasIcon;
import com.scurab.android.myplaces.util.AppUtils;

public abstract class MapElement implements HasIcon, HasCoordinates {

    private double x;
    private double y;

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public LatLng getLatLng() {
        return new LatLng(y, x);
    }

    public void setX(double x) {
        this.x = AppUtils.round(x, 6);
    }

    public void setY(double y) {
        this.y = AppUtils.round(y, 6);
    }
}
