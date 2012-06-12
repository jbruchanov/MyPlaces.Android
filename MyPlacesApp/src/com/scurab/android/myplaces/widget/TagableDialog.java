package com.scurab.android.myplaces.widget;

import android.app.AlertDialog;
import android.content.Context;

public abstract class TagableDialog extends AlertDialog
{
	private Object mTag;
	
	protected TagableDialog(Context context)
	{
		super(context);
	}

	public Object getTag()
	{
		return mTag;
	}

	public void setTag(Object tag)
	{
		mTag = tag;
	}
	
}
