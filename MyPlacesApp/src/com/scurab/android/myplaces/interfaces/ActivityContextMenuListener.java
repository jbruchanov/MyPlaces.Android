package com.scurab.android.myplaces.interfaces;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;

public interface ActivityContextMenuListener {
    boolean onContextItemSelected(MenuItem item);

    void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo);
}
