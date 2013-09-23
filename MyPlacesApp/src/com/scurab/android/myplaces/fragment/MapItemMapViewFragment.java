package com.scurab.android.myplaces.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.maps.MapView;
import com.scurab.android.myplaces.R;

public class MapItemMapViewFragment extends Fragment {
    private View mContentView;
    private MapView mMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            inflateView(inflater.getContext());
        }
        return mContentView;
    }

    private void inflateView(Context c) {
        if (mContentView == null) {
            mContentView = View.inflate(c, R.layout.mapitem_mapview, null);
            mMapView = (MapView) mContentView.findViewById(R.id.mapView);
        }
    }

    public MapView getMapView(Context c) {
        if (mContentView == null) {
            inflateView(c);
        }
        return mMapView;
    }
}
