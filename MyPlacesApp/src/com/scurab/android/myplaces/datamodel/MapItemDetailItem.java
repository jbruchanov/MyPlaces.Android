package com.scurab.android.myplaces.datamodel;

public class MapItemDetailItem
{
	public final static int TYPE_PRO = 1;
	public final static int TYPE_CON = 2;
	public final static int TYPE_DETAIL = 3;
	
	private String mValue;
	private Detail mDetailValue;
	private int mType;
	
	public MapItemDetailItem(String value, int type)
	{
		mValue = value;
		mType = type;
	}
	
	public MapItemDetailItem(Detail d)
	{
		mDetailValue = d;
		mType = TYPE_DETAIL;
	}
	
	public int getType()
	{
		return mType;
	}
	
	public String getValue()
	{
		return mValue;
	}
	
	public Detail getDetailValue()
	{
		return mDetailValue;
	}
	
}
