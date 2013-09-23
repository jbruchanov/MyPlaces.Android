package com.scurab.android.myplaces.datamodel;

import com.scurab.android.myplaces.R;

public class MyPosition extends MapElement {
    private double x;
    private double y;

    public MyPosition() {
    }

    public MyPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int getIconResId() {
        return R.drawable.ic_launcher;
    }
}
