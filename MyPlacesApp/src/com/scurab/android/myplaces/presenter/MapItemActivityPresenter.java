package com.scurab.android.myplaces.presenter;

import com.scurab.android.myplaces.M;
import com.scurab.android.myplaces.MyPlacesApplication;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.activity.MapItemActivity;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.MapItemDetailItem;
import com.scurab.android.myplaces.fragment.MapItemContextFragment;
import com.scurab.android.myplaces.fragment.MapItemDetailFragment;
import com.scurab.android.myplaces.interfaces.ActivityContextMenuListener;
import com.scurab.android.myplaces.interfaces.ActivityOptionsMenuListener;
import com.scurab.android.myplaces.server.ServerConnection;
import com.scurab.android.myplaces.util.DialogBuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.AsyncTask;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
		bind();
	}
	
	private void bind()
	{
		mContext.setActivityOptionsMenuListener(this);
		mContext.setActivityContextMenuListener(this);
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
		}
		return false;
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
			onAddContextItem(type);
		}
	};
	
	public void onAddContextItem(int type)
	{
		Toast.makeText(mContext, String.valueOf(type), Toast.LENGTH_SHORT).show();
	}

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
		}
		return false;
	}
	
	public void onDeleteContextItem(MenuItem item)
	{
		AdapterContextMenuInfo mi = (AdapterContextMenuInfo) item.getMenuInfo();
		MapItemDetailItem midi = (MapItemDetailItem) mContextFragment.getListView().getItemAtPosition(mi.position);
		Toast.makeText(mContext, "DELETE " + midi.getType() + " " + midi.getValue(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		MenuInflater inflater = mContext.getMenuInflater();
		inflater.inflate(R.menu.menu_delete, menu);
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
			
			if(mTag.equals("1"))
				mDetailFragment = (MapItemDetailFragment) mFragment;
			else if(mTag.equals("3"))
			{
				mContextFragment = (MapItemContextFragment) mFragment;
				if(callSetContext && mDetailedItem != null)
					mContextFragment.setMapItem(mDetailedItem);
			}
			
			if(mMenu != null)
			{
				boolean t3 = mTag.equals("3");
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
			}
		}
	}

	
}
