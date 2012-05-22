package com.scurab.android.myplaces.interfaces;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public interface ActivityContextMenuListener
{
	boolean onContextItemSelected(MenuItem item);
	void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo);
}
