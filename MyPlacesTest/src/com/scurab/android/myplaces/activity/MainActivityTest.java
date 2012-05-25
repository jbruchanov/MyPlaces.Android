package com.scurab.android.myplaces.activity;

import android.test.ActivityInstrumentationTestCase2;
import com.scurab.android.myplaces.activity.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{
	public MainActivityTest()
	{
		super(MainActivity.class);
	} //

	public void testFindViews()
	{
		MainActivity ma = getActivity();

		assertNotNull(ma.getMapView());
		assertNotNull(ma.getProgressBar());
		assertNotNull(ma.getMyLocationButton());
		assertNotNull(ma.getMapItemPanel());
	}
}
