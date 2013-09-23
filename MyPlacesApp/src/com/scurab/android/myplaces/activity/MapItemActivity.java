package com.scurab.android.myplaces.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.google.android.maps.MapView;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.fragment.MapItemContextFragment;
import com.scurab.android.myplaces.fragment.MapItemDetailFragment;
import com.scurab.android.myplaces.fragment.MapItemMapViewFragment;
import com.scurab.android.myplaces.presenter.MapItemActivityPresenter;

public class MapItemActivity extends BaseMapActivity {
    private View mContentView;
    private MapItemActivityPresenter mPresenter;
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        mPresenter = getPresenter();
        init();
    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setDisplayShowTitleEnabled(false);
        Tab tab = actionBar.newTab().setText(R.string.lblMapItem)
                .setTabListener(mPresenter.new TabListener<MapItemDetailFragment>(this, "1", MapItemDetailFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText(R.string.lblMap)
                .setTabListener(mPresenter.new TabListener<MapItemMapViewFragment>(this, "2", MapItemMapViewFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText(R.string.lblContext)
                .setTabListener(mPresenter.new TabListener<MapItemContextFragment>(this, "3", MapItemContextFragment.class));
        actionBar.addTab(tab);

        mMapView = (MapView) findViewById(R.id.mapView);
    }

    @Override
    public View getContentView() {
        if (mContentView == null) {
            mContentView = View.inflate(this, R.layout.mapitem_detail, null);
        }
        return mContentView;
    }

    @Override
    public MapItemActivityPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new MapItemActivityPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public MapView getMapView() {
        return mMapView;
    }
}
