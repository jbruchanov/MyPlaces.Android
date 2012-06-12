package com.scurab.android.myplaces.datamodel;

import java.io.Serializable;
import java.util.Date;

public class Detail implements Serializable
{
	private long id;
	private String what;
	private String detail;
	private Date time;
	
	public String getWhat()
	{
		return what;
	}
	public String getDetail()
	{
		return detail;
	}
	public Date getWhen()
	{
		return time;
	}
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public Date getTime()
	{
		return time;
	}
	public void setWhen(Date time)
	{
		this.time = time;
	}
	public void setWhat(String what)
	{
		this.what = what;
	}
	public void setDetail(String detail)
	{
		this.detail = detail;
	}
	
	public void setValues(Detail anotherOne)
	{
		what = anotherOne.what;
		detail = anotherOne.detail;
		time = anotherOne.time;
	}
	public boolean equalsByValues(Detail value)
	{		
		return compare(what, value.what) 
				&& compare(detail, value.detail)
				&& compare(time, value.time);
	}
	
	private boolean compare(Object a, Object b)
	{
		if(a == b)
			return true;
		else if(a != null && b != null)
		{
				return a.equals(b);
		}
		return false;
	}	
}