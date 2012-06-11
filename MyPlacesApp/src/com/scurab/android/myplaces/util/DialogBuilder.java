package com.scurab.android.myplaces.util;

import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.Star;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

public class DialogBuilder 
{
	public interface OnAddMapItenContextButtonClickListener
	{
		public final static int PRO = 1;
		public final static int CON = 2;
		public final static int DETAIL = 4;
		
		public void onClick(View v, int type);
	}
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
	
	public static AlertDialog getAddMapItemContextDialog(Context context, final OnAddMapItenContextButtonClickListener listener)
	{
		if(context == null)
			throw new NullPointerException("Context is null!");
		if(listener == null)
			throw new NullPointerException("OnAddMapItenContextButtonClickListener is null!");
		
		View v = View.inflate(context, R.layout.dialog_addcontext, null);
		View b = v.findViewById(R.id.ibPro);
		AlertDialog.Builder bldr = new Builder(context);
		bldr.setView(v);
		final AlertDialog ad = bldr.create();
		b.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){ad.dismiss();listener.onClick(v, OnAddMapItenContextButtonClickListener.PRO);}});
		b = v.findViewById(R.id.ibCon);
		b.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){ad.dismiss();listener.onClick(v, OnAddMapItenContextButtonClickListener.CON);}});
		b = v.findViewById(R.id.ibDetail);
		b.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){ad.dismiss();listener.onClick(v, OnAddMapItenContextButtonClickListener.DETAIL);}});
			
		return ad;
	}
}
