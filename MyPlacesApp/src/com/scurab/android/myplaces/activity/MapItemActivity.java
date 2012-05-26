package com.scurab.android.myplaces.activity;

import java.io.ObjectInputStream.GetField;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.scurab.android.myplaces.M;
import com.scurab.android.myplaces.MyPlacesApplication;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.fragment.MapItemContextFragment;
import com.scurab.android.myplaces.fragment.MapItemDetailFragment;
import com.scurab.android.myplaces.fragment.MapItemMapViewFragment;
import com.scurab.android.myplaces.presenter.BasePresenter;
import com.scurab.android.myplaces.server.ServerConnection;

public class MapItemActivity extends BaseMapActivity
{
	private View mContentView;
	private Fragment mCurrentFragment;
	private Menu mMenu;
	private MapItemDetailFragment mDetailFragment;
	private MapItemContextFragment mContextFragment;
	private MapItem mDetailedItem;

	@Override
	protected void onCreate(Bundle b)
	{
		super.onCreate(b);
		init();

	}

	private void init()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setDisplayShowTitleEnabled(false);

		Tab tab = actionBar.newTab().setText(R.string.lblMapItem)
				.setTabListener(new TabListener<MapItemDetailFragment>(this, "1", MapItemDetailFragment.class));
		actionBar.addTab(tab);

		tab = actionBar.newTab().setText(R.string.lblStreetView)
				.setTabListener(new TabListener<MapItemMapViewFragment>(this, "2", MapItemMapViewFragment.class));
		actionBar.addTab(tab);

		tab = actionBar.newTab().setText(R.string.lblContext)
				.setTabListener(new TabListener<MapItemContextFragment>(this, "3", MapItemContextFragment.class));
		actionBar.addTab(tab);
		
		if(getIntent().hasExtra(M.Constants.MAP_ITEM))
		{
			MapItem mi = (MapItem) getIntent().getExtras().get(M.Constants.MAP_ITEM);
			new LoadTask().execute(mi.getId());
		}
	}	
	
	public void selectTab(int indexTab)
	{
		ActionBar ab = getActionBar();
		ab.selectTab(ab.getTabAt(indexTab));
	}

	@Override
	public View getContentView()
	{
		if (mContentView == null)
			mContentView = View.inflate(this, R.layout.mapitem_detail, null);
		return mContentView;
	}

	@Override
	public BasePresenter getPresenter()
	{
		return null;
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
	
	public Fragment getCurrentFragment()
	{
		return mCurrentFragment;
	}

	private class TabListener<T extends Fragment> implements ActionBar.TabListener
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

		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
			if (mFragment != null)
			{
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mapitem, menu);
		mMenu = menu;
		mMenu.findItem(R.id.muAdd).setVisible(false);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
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
					MyPlacesApplication mpa = (MyPlacesApplication) getApplication();
					ServerConnection sc = mpa.getServerConnection();
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
			mDetailFragment.setMapItem(result);
			if(mContextFragment != null)
				mContextFragment.setMapItem(result);
		}
	}
}
