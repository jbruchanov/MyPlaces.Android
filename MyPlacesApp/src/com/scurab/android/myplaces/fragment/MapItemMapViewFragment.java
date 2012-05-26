package com.scurab.android.myplaces.fragment;

import com.google.android.maps.MapView;
import com.scurab.android.myplaces.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapItemMapViewFragment extends Fragment
{
	private View mContentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if(mContentView == null)
		{
			mContentView = inflater.inflate(R.layout.mapitem_mapview,null);
		}
		return mContentView;
	}
}
