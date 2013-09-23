package com.scurab.android.myplaces.overlay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.scurab.android.myplaces.datamodel.MapElement;

public class MyPlaceOverlay<T extends MapElement> extends ItemizedOverlay<MyPlaceOverlayItem> {
    public interface OnTapListener<T> {
        void onTap(MyPlaceOverlay<?> item);
    }

    private T mItem;
    private MyPlaceOverlayItem mOverlay;
    private OnTapListener<T> mTapListener;

    protected MyPlaceOverlay(Drawable d, T s) {
        super(d);
        mOverlay = new MyPlaceOverlayItem(s);
        mItem = s;
        mOverlay.setMarker(boundCenter(d));
        populate();
    }

    public MyPlaceOverlay(Context c, T s) {
        super(c.getResources().getDrawable(s.getIconResId()));
        mOverlay = new MyPlaceOverlayItem(s);
        mItem = s;
        mOverlay.setMarker(boundCenter(c.getResources().getDrawable(s.getIconResId())));
        populate();
    }

    @Override
    protected MyPlaceOverlayItem createItem(int arg0) {
        return mOverlay;
    }

    @Override
    protected boolean onTap(int arg0) {
        if (mTapListener != null) {
            mTapListener.onTap(this);
        }
        return false;
    }

    @Override
    public int size() {
        return 1;
    }

    public void setTapListener(OnTapListener<T> tapListener) {
        mTapListener = tapListener;
    }

    public T getObject() {
        return mItem;
    }
}
