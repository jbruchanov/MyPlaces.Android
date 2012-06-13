package com.scurab.android.myplaces.widget.dialog;

import java.util.Date;

import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.Detail;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MapItemDetailDialog extends TagableDialog
{
	private Context mContext;
	private EditText mTitle;
	private EditText mDate;
	private EditText mText;
	
	private java.text.DateFormat mDateFormat;
	private Detail mDetail;
	
	public MapItemDetailDialog(Context context)
	{
		super(context);
		mContext = context;
		mDateFormat = DateFormat.getDateFormat(mContext);
		build();
		setIcon(R.drawable.ico_pencil);
	}
	
	
	private void build()
	{
		LinearLayout ll = new LinearLayout(mContext);
		ll.setGravity(Gravity.TOP | Gravity.LEFT);
		ll.setOrientation(LinearLayout.VERTICAL);
		ImageView iv = new ImageView(mContext);
		iv.setPadding(0,15,0,15);
		iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ico_pencil));
		
		mTitle = new EditText(mContext);
		mTitle.setSingleLine(true);
		mTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mDate = new EditText(mContext);
		mDate.setText(mDateFormat.format(new Date(System.currentTimeMillis())));
		mDate.setSingleLine(true);
		mDate.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mDate.setEnabled(false);
		
		mText = new EditText(mContext);		
		mText.setMinLines(3);
		mText.setMaxLines(6);
		mText.setSingleLine(false);
		mText.setGravity(Gravity.TOP | Gravity.LEFT);
		mText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		ll.addView(iv);
		ll.addView(mTitle);
		ll.addView(mDate);
		ll.addView(mText);
		
		setView(ll);
		setCancelable(false);	
	}
	
	public void setDetailText(String value)
	{
		mText.setText(value);
	}
	
	public String getDetailText()
	{
		return mText.getText().toString();
	}
	
	public void setDetailTitle(String value)
	{
		mTitle.setText(value);
	}
	
	public String getDetailTitle()
	{
		return mTitle.getText().toString();
	}
	
	public void setDetailDate(String value)
	{
		mDate.setText(value);
	}
	
	public String getDetailDate()
	{
		return mDate.getText().toString();
	}

	public void setDetail(Detail content)
	{
		mDetail = content;
		setDetailTitle(content.getWhat());
		setDetailDate(mDateFormat.format(content.getWhen()));
		setDetailText(content.getDetail());
	}
	
	public Detail getDetail()
	{
		if(mDetail == null)
			mDetail = new Detail();
		
		mDetail.setWhat(getDetailTitle());
		mDetail.setWhen(new Date(System.currentTimeMillis()));
		mDetail.setDetail(getDetailText());
		
		return mDetail;
	}
}
