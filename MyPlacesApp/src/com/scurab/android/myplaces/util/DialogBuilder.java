package com.scurab.android.myplaces.util;

import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.Star;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.Gravity;
import android.widget.EditText;

public class DialogBuilder 
{
	/**
	 * 
	 * @param context 
	 * @param mEditText as contentview
	 * @param s
	 * @return
	 */
	public static AlertDialog getStarDialog(Context context, EditText mEditText, Star s)
	{			
		if(context == null)
			throw new NullPointerException("Context is null!");
		if(s == null)
			throw new NullPointerException("Star is null!");
		
		mEditText.setSingleLine(false);
		mEditText.setLines(5);
		mEditText.setGravity(Gravity.TOP | Gravity.LEFT);
		AlertDialog.Builder b = new Builder(context);
		b.setTitle(R.string.lblNote);
		b.setView(mEditText);	
		mEditText.setText(s.getNote());		
		return b.create();		
	}	
}
