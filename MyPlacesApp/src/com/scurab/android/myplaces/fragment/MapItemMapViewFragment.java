package com.scurab.android.myplaces.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.scurab.android.myplaces.R;

public class MapItemMapViewFragment extends Fragment {
    private View mContentView;
    private GoogleMap mGoogleMap;

    private OnMapInitialized mOnMapInitialized;

    public interface OnMapInitialized{
        void onMapInitialized(GoogleMap map);
    }

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
            mGoogleMap = ((MapFragment) (getActivity()).getFragmentManager().findFragmentById(R.id.map)).getMap();
            if(mOnMapInitialized  != null){
                mOnMapInitialized.onMapInitialized(mGoogleMap);
            }
        }
    }

    public void setOnMapInitialized(OnMapInitialized onMapInitialized) {
        mOnMapInitialized = onMapInitialized;
    }

    public GoogleMap getMapView() {
        return mGoogleMap;
    }
}
