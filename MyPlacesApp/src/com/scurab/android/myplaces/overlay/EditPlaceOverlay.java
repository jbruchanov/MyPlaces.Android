package com.scurab.android.myplaces.overlay;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.MapElement;

public class EditPlaceOverlay<T extends MapElement> extends MyPlaceOverlay<T>
{
	public EditPlaceOverlay(Context d, T s)
	{
		super(d.getResources().getDrawable(R.drawable.ico_pin), s);
	}
}
