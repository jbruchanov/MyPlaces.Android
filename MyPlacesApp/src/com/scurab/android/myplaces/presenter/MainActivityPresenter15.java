package com.scurab.android.myplaces.presenter;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.scurab.android.myplaces.M;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.activity.MainActivity;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.MyPosition;
import com.scurab.android.myplaces.datamodel.Star;
import com.scurab.android.myplaces.interfaces.ActivityLifecycleListener;
import com.scurab.android.myplaces.interfaces.ActivityOnBackPressed;
import com.scurab.android.myplaces.interfaces.ActivityOptionsMenuListener;
import com.scurab.android.myplaces.interfaces.OnLocationListener;
import com.scurab.android.myplaces.overlay.MyPlaceOverlay;
import com.scurab.android.myplaces.util.AppUtils;
import com.scurab.android.myplaces.util.DialogBuilder;
import com.scurab.android.myplaces.widget.MapItemPanel;
import com.scurab.android.myplaces.widget.SmileyDialog;

public class MainActivityPresenter15 extends BasePresenter implements ActivityOptionsMenuListener, ActivityLifecycleListener, ActivityOnBackPressed
{
	public static final int STATE_DEFAULT = 0;
	public static final int STATE_ADDING_NEW_ITEM = 1;
	public static final int STATE_ADDING_NEW_STAR = 2;
	
	public static final int DIALOG_STAR = 0x482489f;
	private MainActivity mContext;
	
	private int mState = STATE_DEFAULT;
	
	private MapView mMapView;
	private PresenterHandler mHandler;
	private MyPlaceOverlay<MyPosition> mMyLocationOverlay;
	private List<MyPlaceOverlay<Star>> mStarOverlays;
	private List<MyPlaceOverlay<MapItem>> mMapItemOverlays;
	
	private View.OnTouchListener mMapTouchListener = new View.OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			boolean result = false;
			if(event.getAction() == MotionEvent.ACTION_UP)
			{				
	            Projection proj = mMapView.getProjection();
	            GeoPoint loc = proj.fromPixels((int)event.getX(), (int)event.getY());
				result = onMapClick(loc);
			}
			return result;
		}
	};
	
	private PopupMenu.OnMenuItemClickListener mAddPopupMenuListener = new PopupMenu.OnMenuItemClickListener()
	{
		@Override
		public boolean onMenuItemClick(MenuItem item)
		{
			return onAddMenuItemClick(item.getItemId());			
		}
	};
	
	
	public MainActivityPresenter15(MainActivity context)
	{
		super(context);
		mContext = context;
		init();
		bind();
	}
	
	private void bind()
	{
		mContext.setActivityOptionsMenuListener(this);
		mContext.setActivityListener(this);
		if(mMapView != null) //null is only in tests!
			mMapView.setOnTouchListener(mMapTouchListener);
		
		mContext.getMyLocationButton().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{		
				onMyLocationClick();
			}
		});
		
		mContext.setOnBackPressedListener(this);
	}
	
	public void onMyLocationClick()
	{		
		showProgressBar();
		Location l = getMyLocation(new OnLocationListener()
		{
			@Override
			public void onLocationFound(String provider, Location l)
			{			
				hideProgressBar();
				onLocationResult(l);
			}
		});
		onLocationResult(l);
		mHandler.sendEmptyMessageDelayed(PresenterHandler.HIDE_PROGRESSBAR, 15000);
	}
	
	private void onLocationResult(Location l)
	{
		if(l == null)
			return;
		GeoPoint center = mMapView.getProjection().fromPixels(mMapView.getWidth()/2, mMapView.getHeight()/2);		
		GeoPoint gp = new GeoPoint((int)(l.getLatitude()*M.COORD_HELP_MAPPER),
								   (int)(l.getLongitude()*M.COORD_HELP_MAPPER));		
		double distance = AppUtils.getDistance(center, gp);
		int zoom = 19;
		if(distance > 1000 || mMapView.getZoomLevel() != zoom) //approx half of display with zoom 19
		{
			MapController c = mMapView.getController();
			c.setZoom(zoom);
			c.animateTo(gp);					
			mHandler.sendDelayedRemoveMyOverlay();
//			Toast.makeText(mContext, "centered " + System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
		}
		addMyLocationOverlay(l.getLongitude(),l.getLatitude());
	}

	private void init()
	{
		mMapView = mContext.getMapView();
		mHandler = createHandler(mContext);
		mStarOverlays = new ArrayList<MyPlaceOverlay<Star>>();
		mMapItemOverlays = new ArrayList<MyPlaceOverlay<MapItem>>();
		loadData();
	}
	
	protected PresenterHandler createHandler(Context c)
	{
		return new PresenterHandler(mContext);
	}
	
	protected void loadData()
	{
		onLoadingStars();
		onLoadingMapItems();
	}
	
	/**
	 * Stars async loading stars method
	 */
	public void onLoadingStars()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				onLoadingStarsImpl();
			}
		},"onLoadingStars").start();
	}
	
	/**
	 * Stars async loading map items method
	 */
	public void onLoadingMapItems()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				onLoadingMapItemsImpl();
			}
		},"onLoadingStars").start();
	}
	
	/**
	 * Implementation of loading<br/>
	 * Blocking method!
	 */
	protected void onLoadingStarsImpl()
	{
		try
		{
			Star[] stars = getServerConnection().getStars();
			onLoadedStars(stars);
		}
		catch(Exception e)
		{
			showMessage(e);
		}
	}
	
	/**
	 * Returns list of map overlays
	 * @return
	 */
	protected List<Overlay> getOverlayList()
	{
		return mContext.getMapView().getOverlays();
	}
	
	public boolean onMapClick(GeoPoint location)
	{
		int state = getState();
		boolean result = (state == STATE_ADDING_NEW_ITEM || state == STATE_ADDING_NEW_STAR);
		if(result)
		{
			if(state == STATE_ADDING_NEW_STAR)
			{
				showSmileyDialog(location);
			}
			setState(STATE_DEFAULT);
		}
		return result;
	}
	
	protected void showSmileyDialog(GeoPoint location)
	{
		SmileyDialog sd = new SmileyDialog(mContext, location);
		sd.setOnClickListener(new Dialog.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{				
				SmileyDialog sd = (SmileyDialog) dialog;
				GeoPoint location = sd.getGeoPoint();
//				showMessage(String.format("ic:%S x:%s y:%s", which, location.getLatitudeE6()/M.COORD_HELP_MAPPER,location.getLongitudeE6()/M.COORD_HELP_MAPPER));
				onAddNewStar(location, which);
			}
		});
		sd.setOnCancelListener(new Dialog.OnCancelListener(){@Override public void onCancel(DialogInterface dialog){setState(STATE_DEFAULT);}});
		sd.show();
	}
	
	public void onAddNewStar(final GeoPoint location, final int starIconId)
	{
		setState(STATE_DEFAULT);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				final Star s = new Star();
				s.setX(location.getLongitudeE6()/M.COORD_HELP_MAPPER);
				s.setY(location.getLatitudeE6()/M.COORD_HELP_MAPPER);
				s.setType(Star.getStarTypeByIconId(starIconId));
				Star saved = getServerConnection().save(s);
				s.setId(saved.getId());
				mContext.runOnUiThread(new Runnable(){@Override public void run(){onLoadedStars(new Star[] {s});/*mMapView.invalidate();*/}});
				mHandler.sendEmptyMessage(PresenterHandler.INVALIDATE_MAP);
			}
		},"onAddNewStar").start();
	}
	
	/**
	 * Handler for showing stars to map
	 * @param stars
	 */
	public void onLoadedStars(Star[] stars)
	{
		if(stars.length == 0)
			return;
		List<Overlay> mapOverlays = getOverlayList();
		synchronized (mapOverlays)
		{
			for(Star s : stars)
			{
				MyPlaceOverlay<Star> so = new MyPlaceOverlay<Star>(mContext, s);
				so.setTapListener(new MyPlaceOverlay.OnTapListener<Star>(){
					@SuppressWarnings("unchecked")
					@Override public void onTap(MyPlaceOverlay<?> item){onStarTap((MyPlaceOverlay<Star>) item);}}
				);
				mapOverlays.add(so);
				mStarOverlays.add(so);
			}
		}
		mHandler.sendEmptyMessage(PresenterHandler.INVALIDATE_MAP);
	}
	
	/**
	 * Shows edit dialog to update note
	 * @param s
	 * @return
	 */
	public boolean onStarTap(MyPlaceOverlay<Star> overlay)
	{				
		showStarDialog(overlay);
		return true;
	}
	
	protected void showStarDialog(final MyPlaceOverlay<Star> overlay)
	{
		final Star star = overlay.getObject();
		final EditText mEditText = new EditText(mContext);
		final AlertDialog ad = DialogBuilder.getStarDialog(mContext, mEditText, star);		
		//update handler
		ad.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.lblOK), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{				
				String s = mEditText.getText().toString();
				if(!s.equals(star.getNote()))
				{
					star.setNote(s);
					showProgressBar();
					new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								getServerConnection().save(star);
							}
							catch(Exception e)
							{
								showMessage(e);
							}
							hideProgressBar();
						}
					},"UpdateStar").run();
				}
			}
		});
		
		//delete handler
		ad.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getString(R.string.lblDeleteStar), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				showProgressBar();				
				mStarOverlays.remove(overlay);
				getOverlayList().remove(overlay);
				mMapView.invalidate();
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							getServerConnection().delete(star);
						}
						catch(Exception e)
						{
							showMessage(e);
							mStarOverlays.add(overlay);
							getOverlayList().add(overlay);
						}
						hideProgressBar();
					}
				},"UpdateStar").run();
			}
		});
		ad.show();
	}
	
	public boolean onMapItemTap(MyPlaceOverlay<MapItem> overlay)
	{
		MapItemPanel mip = mContext.getMapItemPanel();
		mip.setMapItem(overlay.getObject());
		mContext.getMapItemPanel().show();
		mMapView.getController().animateTo(overlay.getCenter());
		return true;
	}
	
	/**
	 * Implementation of loading<br/>
	 * Blocking method
	 */
	protected void onLoadingMapItemsImpl()
	{
		try
		{
			MapItem[] items = getServerConnection().getMapItems(14, 55, 16, 50);
			onLoadedMapItems(items);
		}
		catch(Exception e)
		{
			showMessage(e);
		}
	}
	
	protected void addMyLocationOverlay(double x, double y)
	{		
		List<Overlay> mapOverlays = getOverlayList();
		if(mMyLocationOverlay != null)
			mapOverlays.remove(mMyLocationOverlay);
		mMyLocationOverlay = new MyPlaceOverlay<MyPosition>(mContext, new MyPosition(x, y));
		mapOverlays.add(mMyLocationOverlay);
	}
	
	protected void removeMyLocationOverlay()
	{
		if(mMyLocationOverlay != null)
		{	List<Overlay> mapOverlays = getOverlayList();
			mapOverlays.remove(mMyLocationOverlay);
		}
		mMyLocationOverlay = null;
		mMapView.invalidate();
	}
	
	/**
	 * Handler for showing mapitems to map
	 * @param items
	 */
	public void onLoadedMapItems(MapItem[] items)
	{
		if(items.length == 0)
			return;

		List<Overlay> mapOverlays = getOverlayList();
		synchronized (mapOverlays)
		{
			for(MapItem s : items)
			{
				MyPlaceOverlay<MapItem> so = new MyPlaceOverlay<MapItem>(mContext, s);
				so.setTapListener(new MyPlaceOverlay.OnTapListener<MapItem>(){
					@SuppressWarnings("unchecked")
					@Override public void onTap(MyPlaceOverlay<?> item){onMapItemTap((MyPlaceOverlay<MapItem>) item);}}
				);
				mMapItemOverlays.add(so);
				mapOverlays.add(so);
			}
		}
		mHandler.sendEmptyMessage(PresenterHandler.INVALIDATE_MAP);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean result = true;
		switch(item.getItemId())
		{
			case R.id.muAdd:					
				PopupMenu pm = getPopupMenu(R.menu.menu_add,R.id.muAdd);
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
	 * @param item
	 * @return
	 */
	public boolean onAddMenuItemClick(int itemId)
	{
		boolean result = true;
		switch(itemId)
		{
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
	 * @return
	 */
	protected int getState()
	{
		return mState;
	}
	
	/**
	 * Sets current state {@value #STATE_DEFAULT} ...
	 * @param value
	 */
	protected void setState(int value)
	{
		mState = value;
	}
	
	private PopupMenu getPopupMenu(int resMenu, int resAnchorId)
	{
		View v = mContext.findViewById(resAnchorId);
		PopupMenu pm = new PopupMenu(mContext, v);
		pm.getMenuInflater().inflate(resMenu, pm.getMenu());
		return pm;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = mContext.getMenuInflater();
		inflater.inflate(R.menu.main_activity, menu);
		final SearchView searchView = (SearchView) menu.findItem(R.id.muSearch).getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextSubmit(String query)
			{
				onSearch(query);				
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText){return false;}
		});
		return true;
	}

	public void onSearch(String query)
	{
		try
		{
			List<Address> result = getLocations(query);
			if(result.size() == 1)
			{
				Address a = result.get(0);				
				GeoPoint gp = new GeoPoint((int)(a.getLatitude()*M.COORD_HELP_MAPPER),
						   				   (int)(a.getLongitude()*M.COORD_HELP_MAPPER));
				mMapView.getController().setCenter(gp);
				mMapView.getController().setZoom(10);
			}
			else
			{
				Toast.makeText(mContext, "Found " + result.size(), Toast.LENGTH_SHORT).show();
			}
		}
		catch(Exception e)
		{
			showMessage(e);
		}
	}

	@Override
	public void onStart()
	{
		mContext.getMyLocationButton().setVisibility(isFineGeolocationEnabled() ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onStop()
	{
		
	}
	
	public void showProgressBar()
	{
		mContext.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mContext.getProgressBar().setVisibility(View.VISIBLE);
			}
		});
	}
	
	public void hideProgressBar()
	{
		mContext.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mContext.getProgressBar().setVisibility(View.GONE);
			}
		});
	}
	
	protected class PresenterHandler extends Handler
	{		
		public static final int HIDE_PROGRESSBAR = 1;
		public static final int REMOVE_MYLOCATION_OVERLAY = 2;
		public static final int INVALIDATE_MAP = 3;
		
		public PresenterHandler(Context c)
		{
			super(c.getMainLooper());
		}
		
		public void sendDelayedRemoveMyOverlay()
		{
			removeMessages(REMOVE_MYLOCATION_OVERLAY);
			sendEmptyMessageDelayed(REMOVE_MYLOCATION_OVERLAY, 10000);
		}

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case HIDE_PROGRESSBAR:
					hideProgressBar();
					break;
				case REMOVE_MYLOCATION_OVERLAY:
					removeMyLocationOverlay();
					break;
				case INVALIDATE_MAP:
					mMapView.invalidate();
					break;
				default:
					super.handleMessage(msg);
			}
		}
	}

	@Override
	public boolean onBackPressed()
	{		
		MapItemPanel mip = mContext.getMapItemPanel();
		boolean res = mip.isVisible();
		if(res)
			mip.hide();
		return res;
	}
}
