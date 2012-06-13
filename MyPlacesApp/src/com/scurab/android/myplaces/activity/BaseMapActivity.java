package com.scurab.android.myplaces.activity;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import com.google.android.maps.MapActivity;
import com.scurab.android.myplaces.interfaces.ActivityContextMenuListener;
import com.scurab.android.myplaces.interfaces.ActivityKeyListener;
import com.scurab.android.myplaces.interfaces.ActivityLifecycleListener;
import com.scurab.android.myplaces.interfaces.ActivityOnBackPressed;
import com.scurab.android.myplaces.interfaces.ActivityOptionsMenuListener;
import com.scurab.android.myplaces.interfaces.ActivityResultListener;
import com.scurab.android.myplaces.presenter.BasePresenter;

public abstract class BaseMapActivity extends MapActivity
{
	public abstract View getContentView();
	public abstract BasePresenter getPresenter();
	private ActivityContextMenuListener mContextListener;
	private ActivityOptionsMenuListener mOptionsListener;
	private ActivityLifecycleListener mActivityListener;
	private ActivityOnBackPressed mOnBackPressedListener;
	private ActivityResultListener mActivityResultListener;
	private ActivityKeyListener mActivityKeyListener;
	
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
	
	@Override
	public void onBackPressed()
	{		
		boolean b = false;
		if(mOnBackPressedListener != null)
			b = mOnBackPressedListener.onBackPressed();
		if(!b)
			super.onBackPressed();
	}
	
	public void setOnBackPressedListener(ActivityOnBackPressed onBackPressedListener)
	{
		mOnBackPressedListener = onBackPressedListener;
	}
	
	public void setActivityOnResultListener(ActivityResultListener listener)
	{
		mActivityResultListener = listener;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(mActivityResultListener != null)
			mActivityResultListener.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	public void setActivityKeyListener(ActivityKeyListener activityKeyListener)
	{
		mActivityKeyListener = activityKeyListener;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		boolean b = false;
		if(mActivityKeyListener != null)
			b = mActivityKeyListener.onKeyDown(keyCode, event);
		if(!b)
			b = super.onKeyDown(keyCode, event);
		return b;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		boolean b = false;
		if(mActivityKeyListener != null)
			b = mActivityKeyListener.onKeyUp(keyCode, event);
		if(!b)
			b = super.onKeyUp(keyCode, event);
		return b;
	}
}
