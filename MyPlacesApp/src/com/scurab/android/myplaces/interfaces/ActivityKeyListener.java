package com.scurab.android.myplaces.interfaces;

import android.view.KeyEvent;

public interface ActivityKeyListener
{
	public boolean onKeyDown(int keyCode, KeyEvent event);
	
	public boolean onKeyUp(int keyCode, KeyEvent event);

}
