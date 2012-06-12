package com.scurab.android.myplaces.presenter;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;
import com.scurab.android.myplaces.M;
import com.scurab.android.myplaces.MyPlacesApplication;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.activity.MapItemActivity;
import com.scurab.android.myplaces.adapter.DetailAdapter;
import com.scurab.android.myplaces.datamodel.Detail;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.MapItemDetailItem;
import com.scurab.android.myplaces.fragment.MapItemContextFragment;
import com.scurab.android.myplaces.fragment.MapItemDetailFragment;
import com.scurab.android.myplaces.fragment.MapItemMapViewFragment;
import com.scurab.android.myplaces.interfaces.ActivityContextMenuListener;
import com.scurab.android.myplaces.interfaces.ActivityOptionsMenuListener;
import com.scurab.android.myplaces.overlay.EditPlaceOverlay;
import com.scurab.android.myplaces.overlay.MyPlaceOverlay;
import com.scurab.android.myplaces.overlay.MyPlaceOverlayItem;
import com.scurab.android.myplaces.server.ServerConnection;
import com.scurab.android.myplaces.util.DialogBuilder;
import com.scurab.android.myplaces.widget.EditTextDialog;
import com.scurab.android.myplaces.widget.MapItemDetailDialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class MapItemActivityPresenter extends BasePresenter implements ActivityOptionsMenuListener, ActivityContextMenuListener
{
	private MapItemActivity mContext;
	private MapItem mDetailedItem;
	private String[] mMapItemTypes = null;
	private Menu mMenu;
	private Fragment mCurrentFragment;
	private MapItemDetailFragment mDetailFragment;
	private MapItemContextFragment mContextFragment;
	private MapView mMapView;
	private EditPlaceOverlay<MapItem> mCurrentMapItem;
	
	public MapItemActivityPresenter(MapItemActivity context)
	{
		super(context);
		mContext = context;
		init();
	}
	
	private void init()
	{
		if(mContext.getIntent().hasExtra(M.Constants.MAP_ITEM))
		{
			mDetailedItem = (MapItem) mContext.getIntent().getExtras().get(M.Constants.MAP_ITEM);
			new LoadTask().execute(mDetailedItem.getId());
		}
		else
		{
			mDetailedItem = new MapItem();
			mCurrentMapItem = new EditPlaceOverlay<MapItem>(mContext, mDetailedItem);
		}
		
		bind();
	}
	
	private void bind()
	{
		mContext.setActivityOptionsMenuListener(this);
		mContext.setActivityContextMenuListener(this);		
	}
	
	private View.OnTouchListener mMapTouchListener = new View.OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			boolean result = false;
			if(event.getAction() == MotionEvent.ACTION_UP)
			{				
				long start = event.getDownTime();
				long end = event.getEventTime();
				if(end - start < 150) //click is shorter than 100ms
				{
		            Projection proj = mMapView.getProjection();
		            GeoPoint loc = proj.fromPixels((int)event.getX(), (int)event.getY());
					onMapClick(loc);				
					result = true;
				}
			}
			return result;
		}
	};
	
	public void onMapClick(GeoPoint loc)
	{
		mDetailedItem.setX(loc.getLongitudeE6()/M.COORD_HELP_MAPPER);
		mDetailedItem.setY(loc.getLatitudeE6()/M.COORD_HELP_MAPPER);		
		mCurrentMapItem = new EditPlaceOverlay<MapItem>(mContext, mDetailedItem);
		setCurrentMapItemToMap(false);
		mMapView.invalidate();
		mDetailFragment.setMapItem(mDetailedItem);
	}
	
	public void selectTab(int indexTab)
	{
		ActionBar ab = mContext.getActionBar();
		ab.selectTab(ab.getTabAt(indexTab));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.muAdd:
				onShowAddContextItemChooser();
				break;
			case R.id.muSave:
				onSaveMapItem();
				break;
			case R.id.muDelete:
				onDeleteMapItem();
				break;
		}
		return false;
	}
	
	public void onSaveMapItem()
	{
		Toast.makeText(mContext, "onSaveMapItem", Toast.LENGTH_SHORT).show();
	}
	
	public void onDeleteMapItem()
	{
		Toast.makeText(mContext, "onDeleteMapItem", Toast.LENGTH_SHORT).show();
	}
	
	public void onShowAddContextItemChooser()
	{
		AlertDialog ad = DialogBuilder.getAddMapItemContextDialog(mContext, mOnAddMapItenContextButtonClickListener);
		ad.show();
	}
	
	private DialogBuilder.OnAddMapItenContextButtonClickListener mOnAddMapItenContextButtonClickListener = new DialogBuilder.OnAddMapItenContextButtonClickListener()	
	{
		@Override
		public void onClick(View v, int type)
		{
			onAddingContextItem(type);
		}
	};
	
	public void onUpdateContextItem(EditTextDialog etd)
	{
		int type = etd.getType();
		String newValue = etd.getText();
		
		MapItemDetailItem midi = (MapItemDetailItem) etd.getTag();
		if(midi != null)//edit
		{
			String oldValue = midi.getValue();
			midi.setValue(newValue);
			if(type == MapItemDetailItem.TYPE_PRO)
			{
				mDetailedItem.getPros().remove(oldValue);
				mDetailedItem.getPros().add(newValue);				
			}
			else if(type == MapItemDetailItem.TYPE_CON)
			{
				mDetailedItem.getCons().remove(oldValue);
				mDetailedItem.getCons().add(newValue);
			}
		}
		else
		{			
			if(type == MapItemDetailItem.TYPE_PRO)
			{
				mDetailedItem.addPro(newValue);
			}
			else if(type == MapItemDetailItem.TYPE_CON)
			{
				mDetailedItem.addCon(newValue);
			}
		}
		DetailAdapter mdi = (DetailAdapter) mContextFragment.getListView().getAdapter(); 
		mdi.notifyDataSetChanged();
	}
	
	public void onUpdateContextItem(MapItemDetailDialog mdd)
	{
		MapItemDetailItem midi = (MapItemDetailItem) mdd.getTag();
		Detail newValue = mdd.getDetail();
		if(midi != null)//edit
		{
			Detail oldDetail = midi.getDetailValue();
			oldDetail.setValues(newValue);
		}
		else
		{
			mDetailedItem.addDetail(newValue);
		}
		DetailAdapter mdi = (DetailAdapter) mContextFragment.getListView().getAdapter(); 
		mdi.notifyDataSetChanged();
	}
	
	public void onAddingContextItem(final int type)
	{
		AlertDialog ad = null;
		if(type == DialogBuilder.OnAddMapItenContextButtonClickListener.DETAIL)
		{
			ad = DialogBuilder.getMapItemContextDialog(mContext, mOnAddProConValueDialogListener, null);
		}
		else
		{
			int ico = (type == DialogBuilder.OnAddMapItenContextButtonClickListener.PRO ? R.drawable.ico_plus : R.drawable.ico_minus); 
			ad = DialogBuilder.getMapItemContextDialog(mContext, ico , mOnAddProConValueDialogListener, null);
		}
		ad.show();
	}
	
	private DialogInterface.OnClickListener mOnAddProConValueDialogListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if(which == DialogInterface.BUTTON_POSITIVE)
			{
				if(dialog instanceof EditTextDialog)
				{
					EditTextDialog ed = (EditTextDialog)dialog;
					onUpdateContextItem(ed);
				}
				else if(dialog instanceof MapItemDetailDialog)
				{
					MapItemDetailDialog mdd = (MapItemDetailDialog)dialog;
					onUpdateContextItem(mdd);
				}
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = mContext.getMenuInflater();
		inflater.inflate(R.menu.mapitem, menu);
		mMenu = menu;
		mMenu.findItem(R.id.muAdd).setVisible(false);
		return true;
	}

	public Fragment getCurrentFragment()
	{
		return mCurrentFragment;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.muDelete:
				onDeleteContextItem(item);
				break;
			case R.id.muEdit:
				onEditingContextItem(item);
				break;
		}
		return false;
	}
	
	public void onDeleteContextItem(MenuItem item)
	{
		AdapterContextMenuInfo mi = (AdapterContextMenuInfo) item.getMenuInfo();
		MapItemDetailItem midi = (MapItemDetailItem) mContextFragment.getListView().getItemAtPosition(mi.position);
		int type = midi.getType();
		if(type == MapItemDetailItem.TYPE_CON)
			mDetailedItem.getCons().remove(midi.getValue());
		else if(type == MapItemDetailItem.TYPE_PRO)
			mDetailedItem.getPros().remove(midi.getValue());
		
		DetailAdapter mdi = (DetailAdapter) mContextFragment.getListView().getAdapter();
		mdi.remove(midi);
	}
	
	public void onEditingContextItem(MenuItem item)
	{
		AdapterContextMenuInfo mi = (AdapterContextMenuInfo) item.getMenuInfo();
		MapItemDetailItem midi = (MapItemDetailItem) mContextFragment.getListView().getItemAtPosition(mi.position);
		int type = midi.getType();
		AlertDialog ad = null;
		if(type != MapItemDetailItem.TYPE_DETAIL)
		{
			int ico = ( type == DialogBuilder.OnAddMapItenContextButtonClickListener.PRO ? R.drawable.ico_plus : R.drawable.ico_minus); 
			EditTextDialog etd = DialogBuilder.getMapItemContextDialog(mContext, ico , mOnAddProConValueDialogListener, midi.getValue());
			etd.setTag(midi);
			ad = etd;
		}
		else
		{
			MapItemDetailDialog d = DialogBuilder.getMapItemContextDialog(mContext, mOnAddProConValueDialogListener, midi.getDetailValue());
			d.setTag(midi);
			ad = d;
		}
		ad.show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		MenuInflater inflater = mContext.getMenuInflater();
		inflater.inflate(R.menu.menu_editdelete, menu);
	}
	
	public class TabListener<T extends Fragment> implements ActionBar.TabListener
	{
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		public TabListener(Activity activity, String tag, Class<T> clz)
		{
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			boolean callSetContext = false;
			if (mFragment == null)
			{
				mFragment = Fragment.instantiate(mActivity, mClass.getName());								
				ft.add(android.R.id.content, mFragment, mTag);
				callSetContext = (mTag.equals("3"));
			}
			else
			{
				ft.attach(mFragment);
			}
			mCurrentFragment = mFragment;
			
			boolean t3 = false;
			if("1".equals(mTag))
			{
				mDetailFragment = (MapItemDetailFragment) mFragment;
			}
			else if("2".equals(mTag))
			{				
				mMapView = ((MapItemMapViewFragment) mFragment).getMapView(mContext);
				mMapView.setOnTouchListener(mMapTouchListener);
				setCurrentMapItemToMap(true);
			}
			else if("3".equals(mTag))
			{
				t3 = true;
				mContextFragment = (MapItemContextFragment) mFragment;
				if(callSetContext && mDetailedItem != null)
					mContextFragment.setMapItem(mDetailedItem);
			}
			
			if(mMenu != null)
			{
				mMenu.findItem(R.id.muAdd).setVisible(t3);
				mMenu.findItem(R.id.muDelete).setVisible(!t3);
				mMenu.findItem(R.id.muSave).setVisible(!t3);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
			if (mFragment != null)
			{
				ft.detach(mFragment);
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
			
		}
	}
	
	private class LoadTask extends AsyncTask<Long, Void, MapItem> 
	{
		@Override
		protected MapItem doInBackground(Long... params)
		{
			MapItem detailed = null;
			if(params.length == 1)
			{
				try
				{
					long id = params[0];
					MyPlacesApplication mpa = (MyPlacesApplication) mContext.getApplication();
					ServerConnection sc = mpa.getServerConnection();
					if(mMapItemTypes == null)
						mMapItemTypes = sc.getMapItemTypes();
					detailed = sc.getDetailedMapItem(id);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			return detailed;
		}
		
		@Override
		protected void onPostExecute(MapItem result)
		{			
			mDetailedItem = result;
			if(result != null)
			{
//				MapItemDetailFragment mDetailFragment = mContext.getDetailFragment();
//				MapItemContextFragment mContextFragment = mContext.getContextFragment();
				
				mDetailFragment.setMapItemTypes(mMapItemTypes);
				mDetailFragment.setMapItem(result);
				
				if(mContextFragment != null)
					mContextFragment.setMapItem(result);
				
				mContext.setTitle(mDetailedItem.getTitle());				
				mCurrentMapItem = new EditPlaceOverlay<MapItem>(mContext, result);
				if(mMapView != null)
				{
					setCurrentMapItemToMap(true);
				}
			}
		}
	}
	
	private void setCurrentMapItemToMap(boolean move)
	{
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(mCurrentMapItem);
		if(move)
		{
			MapController mc = mMapView.getController();
			mc.setCenter(mCurrentMapItem.getCenter());
		}
	}
}
