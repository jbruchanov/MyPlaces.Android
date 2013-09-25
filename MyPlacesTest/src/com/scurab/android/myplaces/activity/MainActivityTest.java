package com.scurab.android.myplaces.activity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{
	public MainActivityTest()
	{
		super(MainActivity.class);
	} //

	public void testFindViews()
	{
		MainActivity ma = getActivity();

		assertNotNull(ma.getMap());
		assertNotNull(ma.getProgressBar());
		assertNotNull(ma.getMyLocationButton());
		assertNotNull(ma.getMapItemPanel());
	}
}
