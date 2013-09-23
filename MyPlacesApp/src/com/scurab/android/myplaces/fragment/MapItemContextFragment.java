package com.scurab.android.myplaces.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.adapter.DetailAdapter;
import com.scurab.android.myplaces.datamodel.MapItem;

public class MapItemContextFragment extends Fragment {
    private View mContentView;
    private ListView lvData;
    private MapItem mMapItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(R.layout.mapitem_context, null);
            lvData = (ListView) mContentView.findViewById(R.id.lvData);
            getActivity().registerForContextMenu(lvData);
            if (mMapItem != null) {
                lvData.setAdapter(new DetailAdapter(getActivity(), mMapItem.getDataForAdapter()));
            }
        }
        return mContentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mMapItem != null) {
            if (lvData != null) {
                lvData.setAdapter(new DetailAdapter(activity, mMapItem.getDataForAdapter()));
            }
        }
    }

    public void setMapItem(MapItem result) {
        mMapItem = result;
        if (getActivity() != null) {
            lvData.setAdapter(new DetailAdapter(getActivity(), result.getDataForAdapter()));
        }
    }

    public ListView getListView() {
        return lvData;
    }
}
