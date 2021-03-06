package com.scurab.android.myplaces.presenter;

import android.app.Activity;
import android.content.Context;
import android.test.AndroidTestCase;

public class BasePresenterTest extends AndroidTestCase
{
	public void testBasePresenterGetServerConnection()
	{		
		MockBasePresenter mbp = new MockBasePresenter(new MockActivity(mContext));
		assertNotNull(mbp.getServerConnection());
	}
	
	public void testBasePresenterGetPropertyProvider()
	{
		MockBasePresenter mbp = new MockBasePresenter(new MockActivity(mContext));
		assertNotNull(mbp.getPropertyProvider());
	}
	
	
	private class MockActivity extends Activity
	{
		public MockActivity(Context newBase)
		{
			attachBaseContext(newBase);
		}
	}
	
	private class MockBasePresenter extends BasePresenter
	{

		public MockBasePresenter(Activity context)
		{
			super(context);
		}
	}
}
