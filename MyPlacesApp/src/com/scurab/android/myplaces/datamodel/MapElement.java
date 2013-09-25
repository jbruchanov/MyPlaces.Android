package com.scurab.android.myplaces.datamodel;

import com.google.android.gms.maps.model.LatLng;
import com.scurab.android.myplaces.interfaces.HasCoordinates;
import com.scurab.android.myplaces.interfaces.HasIcon;
import com.scurab.android.myplaces.util.AppUtils;

import java.io.Serializable;

public abstract class MapElement implements Serializable, HasIcon, HasCoordinates {

    private long id;

    private double x;
    private double y;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
