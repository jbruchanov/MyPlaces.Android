package com.scurab.android.myplaces.presenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.scurab.android.myplaces.M;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.activity.MainActivity;
import com.scurab.android.myplaces.activity.MapItemActivity;
import com.scurab.android.myplaces.datamodel.MapElement;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.Star;
import com.scurab.android.myplaces.interfaces.*;
import com.scurab.android.myplaces.util.DialogBuilder;
import com.scurab.android.myplaces.widget.MapItemPanel;
import com.scurab.android.myplaces.widget.dialog.SmileyDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivityPresenter15 extends BasePresenter implements ActivityOptionsMenuListener, ActivityLifecycleListener, ActivityOnBackPressed, ActivityResultListener, ActivityKeyListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    public static final int STATE_DEFAULT = 0;
    public static final int STATE_ADDING_NEW_ITEM = 1;
    public static final int STATE_ADDING_NEW_STAR = 2;

    public static final int DIALOG_STAR = 0x482489f;
    private MainActivity mContext;

    private int mState = STATE_DEFAULT;

    private GoogleMap mMap;
    private PresenterHandler mHandler;


    private List<Marker> mStars = new ArrayList<Marker>();
    private List<Marker> mPlaces = new ArrayList<Marker>();
    private HashMap<Marker, MapElement> mMarkers = new HashMap<Marker,MapElement>();
    private Marker mMyLocation;

    private SearchView mSearchView;

    private PopupMenu.OnMenuItemClickListener mAddPopupMenuListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return onAddMenuItemClick(item.getItemId());
        }
    };


    public MainActivityPresenter15(MainActivity context) {
        super(context);
        mContext = context;
        init();
        bind();
    }

    private void bind() {
        mContext.setActivityOptionsMenuListener(this);
        mContext.setActivityListener(this);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        mContext.getMyLocationButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMyLocationClick();
            }
        });

        mContext.setOnBackPressedListener(this);

        mContext.getMapItemPanel().setOnMoreButtonClickListener(new MapItemPanel.OnMoreButtonClickListener() {
            @Override
            public void onClick(View v, MapItem item) {
                onMoreButtonClick(item);
            }
        });

        mContext.getReloadButton().setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { onReloadClick(); } });
        mContext.setActivityOnResultListener(this);
        mContext.setActivityKeyListener(this);
    }

    private void onReloadClick() {
        loadData();
        for(Marker m : mMarkers.keySet()){
            m.remove();
        }
        mMarkers.clear();
        mStars.clear();
        mPlaces.clear();
    }

    public void onMyLocationClick() {
        showProgressBar();
        Location l = getMyLocation(new OnLocationListener() {
            @Override
            public void onLocationFound(String provider, Location l) {
                hideProgressBar();
                onLocationResult(l);
            }
        });
        onLocationResult(l);
        mHandler.sendEmptyMessageDelayed(PresenterHandler.HIDE_PROGRESSBAR, 15000);
    }

    private void onLocationResult(Location l) {
        if (l == null) {
            return;
        }
        LatLng newPost = new LatLng(l.getLatitude(), l.getLongitude());

        //FIXME: distance handling
        //LatLng latlng = mMap.getProjection().fromScreenLocation(new Point((int) event.getX(), (int) event.getY()));
        //GeoPoint center = mMap.getProjection().fromPixels(mMap.getWidth() / 2, mMap.getHeight() / 2);
        //double distance = AppUtils.getDistance(newPost, gp);
        int zoom = 15;
        if (/*distance > 1000 ||*/ ((int) mMap.getCameraPosition().zoom) != zoom) //approx half of display with zoom 19
        {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(newPost, zoom);
            mMap.animateCamera(cu);
            mHandler.sendDelayedRemoveMyOverlay();
//			Toast.makeText(mContext, "centered " + System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
        }
        addMyLocationOverlay(l.getLongitude(), l.getLatitude());
    }

    private void init() {
        mMap = mContext.getMap();
        mHandler = createHandler(mContext);
        loadData();
    }

    public void onMoreButtonClick(MapItem item) {
        showMapItemActivity(item);
    }

    protected void showMapItemActivity(MapItem item) {
        Intent i = new Intent(mContext, MapItemActivity.class);
        i.putExtra(M.Constants.MAP_ITEM, item);
//		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivityForResult(i, M.Constants.REQUEST_EDIT_MAP_ITEM);
    }

    protected PresenterHandler createHandler(Context c) {
        return new PresenterHandler(mContext);
    }

    protected void loadData() {
        showProgressBar();
        onLoadingStars();
        onLoadingMapItems();
    }

    /**
     * Stars async loading stars method
     */
    public void onLoadingStars() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                onLoadingStarsImpl();
            }
        }, "onLoadingStars").start();
    }

    /**
     * Stars async loading map items method
     */
    public void onLoadingMapItems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                onLoadingMapItemsImpl();
            }
        }, "onLoadingStars").start();
    }

    /**
     * Implementation of loading<br/>
     * Blocking method!
     */
    protected void onLoadingStarsImpl() {
        try {
            final Star[] stars = getServerConnection().getStars();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onLoadedStars(stars);
                }
            });
        } catch (Exception e) {
            showMessage(e);
        }
    }

    @Override
    public void onMapClick(LatLng location) {
        int state = getState();
        boolean result = (state == STATE_ADDING_NEW_ITEM || state == STATE_ADDING_NEW_STAR);
        if (result) {
            if (state == STATE_ADDING_NEW_STAR) {
                showSmileyDialog(location);
            } else if (state == STATE_ADDING_NEW_ITEM) {
                MapItem mi = new MapItem();
                mi.setX(location.longitude);
                mi.setY(location.latitude);
                showMapItemActivity(mi);
            }
            setState(STATE_DEFAULT);
        }
    }

    protected void showSmileyDialog(LatLng location) {
        SmileyDialog sd = new SmileyDialog(mContext, location);
        sd.setOnClickListener(new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SmileyDialog sd = (SmileyDialog) dialog;
                LatLng location = sd.getLatLng();
//				showMessage(String.format("ic:%S x:%s y:%s", which, location.getLatitudeE6()/M.COORD_HELP_MAPPER,location.getLongitudeE6()/M.COORD_HELP_MAPPER));
                onAddNewStar(location, which);
            }
        });
        sd.setOnCancelListener(new Dialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setState(STATE_DEFAULT);
            }
        });
        sd.show();
    }

    public void onAddNewStar(final LatLng location, final int starIconId) {
        setState(STATE_DEFAULT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Star s = new Star();
                s.setX(location.longitude);
                s.setY(location.latitude);
                s.setType(Star.getStarTypeByIconId(starIconId));
                Star saved = getServerConnection().save(s);
                s.setId(saved.getId());
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLoadedStars(new Star[]{s});/*mMap.invalidate();*/
                    }
                });
//                mHandler.sendEmptyMessage(PresenterHandler.INVALIDATE_MAP);
            }
        }, "onAddNewStar").start();
    }

    /**
     * Handler for showing stars to map
     *
     * @param stars
     */
    public void onLoadedStars(Star[] stars) {
        if (stars.length == 0) {
            return;
        }

        for (Star s : stars) {
            MarkerOptions mo = createStarMarker(s);
            Marker marker = mMap.addMarker(mo);
            mStars.add(marker);
            mMarkers.put(marker, s);
        }
    }

    private MarkerOptions createStarMarker(Star s) {
        final MarkerOptions mo = new MarkerOptions().position(new LatLng(s.getY(), s.getX()))
                .title(s.getTitle())
                .icon(BitmapDescriptorFactory.fromResource(s.getIconResId()))
                .visible(true);
        return mo;
    }

    /**
     * Shows edit dialog to update note
     *
     * @param star
     * @return
     */
    public boolean onStarTap(Marker star) {
        showStarDialog(star);
        return true;
    }

    protected void showStarDialog(final Marker starMarker) {
        final EditText mEditText = new EditText(mContext);
        final Star star = (Star) mMarkers.get(starMarker);
        final AlertDialog ad = DialogBuilder.getStarDialog(mContext, mEditText, star);
        //update handler
        ad.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.lblOK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = mEditText.getText().toString();
                if (!s.equals(star.getNote())) {
                    star.setNote(s);
                    showProgressBar();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getServerConnection().save(star);
                            } catch (Exception e) {
                                showMessage(e);
                            }
                            hideProgressBar();
                        }
                    }, "UpdateStar").run();
                }
            }
        });

        //delete handler
        ad.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getString(R.string.lblDeleteStar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgressBar();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getServerConnection().delete(star);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    starMarker.remove();
                                    mStars.remove(star);
                                }
                            });
                        } catch (Exception e) {
                            showMessage(e);
                        }
                        hideProgressBar();
                    }
                }, "UpdateStar").run();
            }
        });
        ad.setButton(DialogInterface.BUTTON_NEUTRAL, mContext.getString(R.string.lblCancel), (DialogInterface.OnClickListener) null);
        ad.show();
    }

    public boolean onMapItemTap(Marker marker) {
        MapItemPanel mip = mContext.getMapItemPanel();
        MapItem item = (MapItem) mMarkers.get(marker);
        mip.setMapItem(item);
        mContext.getMapItemPanel().show();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(item.getLatLng()));
//        mMap.getController().animateTo(overlay.getCenter());
        return true;
    }

    /**
     * Implementation of loading<br/>
     * Blocking method
     */
    protected void onLoadingMapItemsImpl() {
        try {
//			MapItem[] items = getServerConnection().getMapItems(14, 55, 16, 50);
            final MapItem[] items = getServerConnection().getMapItems(-180, 90, 180, -90);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onLoadedMapItems(items);
                }
            });
        } catch (Exception e) {
            showMessage(e);
        }
    }

    protected void addMyLocationOverlay(double x, double y) {
        if (mMyLocation != null) {
            mMyLocation.remove();
        }

        MarkerOptions mo = createMyLocationMarker(x, y);
        mMyLocation = mMap.addMarker(mo);
    }

    private MarkerOptions createMyLocationMarker(double x, double y) {
        final MarkerOptions mo = new MarkerOptions().position(new LatLng(y, x))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                .visible(true);
        return mo;
    }

    protected void removeMyLocationOverlay() {
        if (mMyLocation != null) {
            mMyLocation.remove();
        }
        mMyLocation = null;
    }

    /**
     * Handler for showing mapitems to map
     *
     * @param items
     */
    public void onLoadedMapItems(MapItem[] items) {
        if (items.length != 0) {
            for (MapItem item : items) {
                MarkerOptions mo = createMapItemMarker(item);
                Marker marker = mMap.addMarker(mo);
                mMarkers.put(marker, item);
                mPlaces.add(marker);
            }
        }
        hideProgressBar();
    }

    private MarkerOptions createMapItemMarker(MapItem item) {
        final MarkerOptions mo = new MarkerOptions().position(new LatLng(item.getY(), item.getX()))
                .title(item.getTitle())
                .icon(BitmapDescriptorFactory.fromResource(item.getIconResId()))
                .visible(true);
        return mo;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case R.id.muAdd:
                PopupMenu pm = getPopupMenu(R.menu.menu_add, R.id.muAdd);
                pm.setOnMenuItemClickListener(mAddPopupMenuListener);
                pm.show();
                break;
            default:
                result = false;
        }
        return result;
    }

    /**
     * Called when Add is clicked from action bar/option menu
     *
     * @param itemId
     * @return
     */
    public boolean onAddMenuItemClick(int itemId) {
        boolean result = true;
        switch (itemId) {
            case R.id.muAddStar:
                setState(STATE_ADDING_NEW_STAR);
                showMessage(R.string.txtClickToMapToSelectPosition);
                break;
            case R.id.muAddMapItem:
                setState(STATE_ADDING_NEW_ITEM);
                showMessage(R.string.txtClickToMapToSelectPosition);
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    /**
     * Returns current state {@value #STATE_DEFAULT} ...
     *
     * @return
     */
    protected int getState() {
        return mState;
    }

    /**
     * Sets current state {@value #STATE_DEFAULT} ...
     *
     * @param value
     */
    protected void setState(int value) {
        mState = value;
    }

    private PopupMenu getPopupMenu(int resMenu, int resAnchorId) {
        View v = mContext.findViewById(resAnchorId);
        PopupMenu pm = new PopupMenu(mContext, v);
        pm.getMenuInflater().inflate(resMenu, pm.getMenu());
        return pm;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = mContext.getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        mSearchView = (SearchView) menu.findItem(R.id.muSearch).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setSubmitButtonEnabled(true);
        return true;
    }

    public void onSearch(String query) {
        try {
            List<Address> result = getLocations(query);
            if (result.size() == 1) {
                Address a = result.get(0);
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(a.getLatitude(), a.getLongitude()), 10);
                mMap.animateCamera(cu);
            } else {
                Toast.makeText(mContext, "Found " + result.size(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            showMessage(e);
        }
    }

    @Override
    public void onStart() {
        mContext.getMyLocationButton().setVisibility(isFineGeolocationEnabled() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStop() {

    }

    public void showProgressBar() {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContext.getProgressBar().setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideProgressBar() {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContext.getProgressBar().setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        MapElement me = mMarkers.get(marker);
        if(me != null){
            if(me instanceof Star){
                onStarTap(marker);
            }else if( me instanceof MapItem){
                onMapItemTap(marker);
            }

            return true;
        }
        return false;
    }

    protected class PresenterHandler extends Handler {
        public static final int HIDE_PROGRESSBAR = 1;
        public static final int REMOVE_MYLOCATION_OVERLAY = 2;

        public PresenterHandler(Context c) {
            super(c.getMainLooper());
        }

        public void sendDelayedRemoveMyOverlay() {
            removeMessages(REMOVE_MYLOCATION_OVERLAY);
            sendEmptyMessageDelayed(REMOVE_MYLOCATION_OVERLAY, 10000);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE_PROGRESSBAR:
                    hideProgressBar();
                    break;
                case REMOVE_MYLOCATION_OVERLAY:
                    removeMyLocationOverlay();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        MapItemPanel mip = mContext.getMapItemPanel();
        boolean res = mip.isVisible();
        if (res) {
            mip.hide();
        }
        return res;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == M.Constants.REQUEST_EDIT_MAP_ITEM) {
            if (resultCode == M.Constants.RESULT_DELETE) {
                MapItem mi = (MapItem) data.getExtras().get(M.Constants.MAP_ITEM);
                onDeletedMapItem(mi);
            } else if (resultCode == M.Constants.RESULT_ADD) {
                MapItem mi = (MapItem) data.getExtras().get(M.Constants.NEW_MAP_ITEM);
                onAddedMapItem(mi);
            } else if (resultCode == M.Constants.RESULT_UPDATE) {
                MapItem oldOne = (MapItem) data.getExtras().get(M.Constants.MAP_ITEM);
                MapItem newOne = (MapItem) data.getExtras().get(M.Constants.NEW_MAP_ITEM);
                onUpdatedMapItem(oldOne, newOne);
            }
        }
    }

    protected void onDeletedMapItem(MapItem deleted) {

        //FIXME:
//        for (MyPlaceOverlay<MapItem> item : mMapItemOverlays) {
//            MapItem onMap = item.getObject();
//            if (onMap.getId() == deleted.getId()) {
//                getOverlayList().remove(item);
//                mMapItemOverlays.remove(item);
////                mMap.invalidate();
//                mContext.getMapItemPanel().hide();
//                break;
//            }
//        }
    }

    protected void onUpdatedMapItem(MapItem oldOne, MapItem newOne) {
        //FIXME:
//        for (MyPlaceOverlay<MapItem> item : mMapItemOverlays) {
//            MapItem onMap = item.getObject();
//            if (onMap.getId() == oldOne.getId()) {
//                getOverlayList().remove(item);
//                mMapItemOverlays.remove(item);
//                break;
//            }
//        }
//        onAddedMapItem(newOne);
//        mContext.getMapItemPanel().setMapItem(newOne);
    }

    protected void onAddedMapItem(MapItem newOne) {
        onLoadedMapItems(new MapItem[]{newOne});
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            if (mSearchView != null) {
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }
}
