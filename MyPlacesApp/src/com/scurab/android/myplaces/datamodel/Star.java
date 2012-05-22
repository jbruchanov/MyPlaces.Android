package com.scurab.android.myplaces.datamodel;

import java.io.Serializable;

import com.scurab.android.myplaces.R;
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
	
	public static int getStarIconResId(String type)
	{
		int result = 0;
		if(type.equals("10"))
			result = R.drawable.ico_star;
		else if(type.equals("11"))
			result = R.drawable.ico_cafe;
		else if(type.equals("12"))
			result = R.drawable.ico_drink;
		else if(type.equals("13"))
			result = R.drawable.ico_wine;
		else if(type.equals("14"))
			result = R.drawable.ico_search;
		else if(type.equals("20"))
			result = R.drawable.ico_smile_happy;
		else if(type.equals("21"))
			result = R.drawable.ico_smile_lick;
		else if(type.equals("22"))
			result = R.drawable.ico_smile_neutral;
		else if(type.equals("23"))
			result = R.drawable.ico_smile_unhappy;
		else if(type.equals("24"))
			result = R.drawable.ico_smile_veryhappy;
		return result;
	}
	
	public int getStarIconResId()
	{
		return getStarIconResId(getType());
	}
}
