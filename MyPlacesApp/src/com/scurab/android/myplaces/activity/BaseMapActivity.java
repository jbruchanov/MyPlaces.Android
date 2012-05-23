package com.scurab.android.myplaces.activity;

import android.app.Application.ActivityLifecycleCallbacks;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import com.google.android.maps.MapActivity;
import com.scurab.android.myplaces.interfaces.ActivityContextMenuListener;
import com.scurab.android.myplaces.interfaces.ActivityLifecycleListener;
import com.scurab.android.myplaces.interfaces.ActivityOptionsMenuListener;
import com.scurab.android.myplaces.presenter.BasePresenter;

public abstract class BaseMapActivity extends MapActivity
{
	public abstract View getContentView();
	public abstract BasePresenter getPresenter();
	private ActivityContextMenuListener mContextListener;
	private ActivityOptionsMenuListener mOptionsListener;
	private ActivityLifecycleListener mActivityListener;
	
	public void setActivityContextMenuListener(ActivityContextMenuListener listener)
	{
		mContextListener = listener;
	}
	
	public void setActivityOptionsMenuListener(ActivityOptionsMenuListener listener)
	{
		mOptionsListener = listener;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{	
		if(mContextListener != null)
			mContextListener.onCreateContextMenu(menu, v, menuInfo);
		else
			super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		if(mContextListener != null)
			return mContextListener.onContextItemSelected(item);
		else
			return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(mOptionsListener != null)
			return mOptionsListener.onOptionsItemSelected(item);
		else
			return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if(mOptionsListener != null)
			return mOptionsListener.onCreateOptionsMenu(menu);
		else
			return super.onCreateOptionsMenu(menu);
	}
	
	public void postInMainThread(Runnable r)
	{
		runOnUiThread(r);
	}

	public void setActivityListener(ActivityLifecycleListener activityListener)
	{
		mActivityListener = activityListener;
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		if(mActivityListener != null)
			mActivityListener.onStart();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		if(mActivityListener != null)
			mActivityListener.onStop();
	}
	
}