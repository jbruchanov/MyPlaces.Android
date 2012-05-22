package com.scurab.android.myplaces.overlay;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.scurab.android.myplaces.interfaces.HasDescription;

public class Overlays<T extends HasDescription> extends ItemizedOverlay<OverlayItem> 
{
	private ArrayList<MyPlaceOverlayItem<T>> mOverlays = new ArrayList<MyPlaceOverlayItem<T>>();
	private Context mContext;
	private int mIconResId;
	public Overlays(Context c, int iconResId)
	{	
		super(boundCenterBottom(c.getResources().getDrawable(iconResId)));
		mContext = c;
		mIconResId = iconResId;		
	}
	
	public int getIconResId()
	{
		return mIconResId;
	}

	@Override
	public OverlayItem createItem(int index)
	{
		return mOverlays.get(index);
	}
	
	public void addOverlay(MyPlaceOverlayItem<T> overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	public void addOverlay(MyPlaceOverlayItem<T> overlay, Drawable d) {
		overlay.setMarker(boundCenterBottom(d));
	    mOverlays.add(overlay);
	    populate();
	}

	@Override
	public int size()
	{
		return mOverlays.size();		
	}
	
	@Override
	protected boolean onTap(int arg0)
	{
		HasDescription h = mOverlays.get(arg0).getObject();
		String desc = h.getDescription();
		Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
		return super.onTap(arg0);		
	}
}
