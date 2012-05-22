package com.scurab.android.myplaces.datamodel;

import java.io.Serializable;

import com.scurab.android.myplaces.interfaces.HasDescription;
import com.scurab.android.myplaces.util.AppUtils;

public class Star implements Serializable, HasDescription
{
	private long id;
	private String note;
	private String type;
	private double x;
	private double y;
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public long getId()
	{
		return id;
	}

	public String getTitle()
	{
		return note;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	
	public void setX(double x)
	{
		this.x = AppUtils.round(x,6);
	}
	public void setY(double y)
	{
		this.y = AppUtils.round(y,6);
	}
	
	public String getIconUrl()
	{
		return "";
//		return getStarUrl(getType());
	}
	
	@Override
	public String getDescription()
	{
		return getTitle() + " " + getType();
	}
	
//	private String getStarUrl(String type)
//	{
//		String result = "";
//		if(type.equals("10"))
//			result = AppConstants.MediumIcons.ICO_STAR;
//		else if(type.equals("11"))
//			result = AppConstants.MediumIcons.ICO_CAFE;
//		else if(type.equals("12"))
//			result = AppConstants.MediumIcons.ICO_DRINK;
//		else if(type.equals("13"))
//			result = AppConstants.MediumIcons.ICO_WINE;
//		else if(type.equals("14"))
//			result = AppConstants.MediumIcons.ICO_SEARCH;
//		else if(type.equals("20"))
//			result = AppConstants.MediumIcons.ICO_SMILE_HAPPY;
//		else if(type.equals("21"))
//			result = AppConstants.MediumIcons.ICO_SMILE_LICK;
//		else if(type.equals("22"))
//			result = AppConstants.MediumIcons.ICO_SMILE_NEUTRAL;
//		else if(type.equals("23"))
//			result = AppConstants.MediumIcons.ICO_SMILE_UNHAPPY;
//		else if(type.equals("24"))
//			result = AppConstants.MediumIcons.ICO_SMILE_VERYHAPPY;
//		return result;
//	}
}
