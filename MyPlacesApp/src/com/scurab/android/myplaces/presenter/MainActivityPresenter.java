package com.scurab.android.myplaces.presenter;

import java.util.List;
import org.restlet.resource.Finder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.activity.MainActivity;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.Star;
import com.scurab.android.myplaces.interfaces.ActivityOptionsMenuListener;
import com.scurab.android.myplaces.overlay.Overlays;
import com.scurab.android.myplaces.overlay.MyPlaceOverlayItem;
import com.scurab.android.myplaces.widget.SmileyDialog;

public class MainActivityPresenter extends BasePresenter implements ActivityOptionsMenuListener
{
	public static final int STATE_DEFAULT = 0;
	public static final int STATE_ADDING_NEW_ITEM = 1;
	public static final int STATE_ADDING_NEW_STAR = 2;
	
	private MainActivity mContext;
	private final static double COORD_HELP_MAPPER = 1E6;
	private int mState = STATE_DEFAULT;
	
	private MapView mMapView;
	
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
	
	public MainActivityPresenter(MainActivity context)
	{
		super(context);
		mContext = context;
		init();
		bind();
	}
	
	private void bind()
	{
		mContext.setActivityOptionsMenuListener(this);
		if(mMapView != null) //null is only in tests!
			mMapView.setOnTouchListener(mMapTouchListener);
	}
	
	private void init()
	{
		mMapView = mContext.getMapView();
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
				showMessage(String.format("ic:%S x:%s y:%s", which, location.getLatitudeE6()/COORD_HELP_MAPPER,location.getLongitudeE6()/COORD_HELP_MAPPER));
			}
		});
		sd.setOnCancelListener(new Dialog.OnCancelListener(){@Override public void onCancel(DialogInterface dialog){setState(STATE_DEFAULT);}});
		sd.show();
	}
	
	public void onAddNewStar(GeoPoint location, int starIconId)
	{
		setState(STATE_DEFAULT);
	}
	
	/**
	 * Handler for showing stars to map
	 * @param stars
	 */
	public void onLoadedStars(Star[] stars)
	{
		List<Overlay> mapOverlays = getOverlayList();
		Overlays<Star> itemizedoverlay = new Overlays<Star>(mContext,R.drawable.ico_star);
		for(Star s : stars)
		{
			int lat = (int)(s.getY() * COORD_HELP_MAPPER);
			int lng = (int)(s.getX() * COORD_HELP_MAPPER);
			MyPlaceOverlayItem<Star> oi = new MyPlaceOverlayItem<Star>(new GeoPoint(lat, lng), s);
			itemizedoverlay.addOverlay(oi);
		}
		mapOverlays.add(itemizedoverlay);
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
	
	/**
	 * Handler for showing mapitems to map
	 * @param items
	 */
	public void onLoadedMapItems(MapItem[] items)
	{
		List<Overlay> mapOverlays = getOverlayList();
		Overlays<MapItem> itemizedoverlay = new Overlays<MapItem>(mContext,R.drawable.ic_launcher);
		Resources res = mContext.getResources();
		for(MapItem s : items)
		{
			int lat = (int)(s.getY() * COORD_HELP_MAPPER);
			int lng = (int)(s.getX() * COORD_HELP_MAPPER);
			MyPlaceOverlayItem<MapItem> oi = new MyPlaceOverlayItem<MapItem>(new GeoPoint(lat, lng), s);
			itemizedoverlay.addOverlay(oi,res.getDrawable(s.getIconResId()));
		}
		mapOverlays.add(itemizedoverlay);
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
		return true;
	}
}
