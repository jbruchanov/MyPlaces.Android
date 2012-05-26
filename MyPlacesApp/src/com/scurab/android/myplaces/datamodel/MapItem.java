package com.scurab.android.myplaces.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.util.AppUtils;

public class MapItem extends MapElement implements Serializable
{
	private long id;
	private String type;
	
	private String name;
	private String country;
	private String city;
	private String street;
	private String web;
	private String streetViewLink;
	private String author;
	private String contact;
	
	private double x;
	private double y;
	
	private int rating;
	
	private List<String> pros;
	private List<String> cons;
	private List<Detail> details;
	
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getStreet()
	{
		return street;
	}
	public void setStreet(String street)
	{
		this.street = street;
	}
	public String getWeb()
	{
		return web;
	}
	public void setWeb(String link)
	{
		web = link;
	}
	public String getStreetViewLink()
	{
		return streetViewLink;
	}
	public void setStreetViewLink(String streetViewLink)
	{
		this.streetViewLink = streetViewLink;
	}
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author)
	{
		this.author = author;
	}
	@Override
	public double getX()
	{
		return x;
	}
	public void setX(double x)
	{
		this.x = AppUtils.round(x,6);
	}
	@Override
	public double getY()
	{
		return y;
	}
	public void setY(double y)
	{
		this.y = AppUtils.round(y,6);
	}
	public List<String> getPros()
	{
		return pros;
	}
	public void setPros(List<String> pros)
	{
		this.pros = pros;
	}
	public List<String> getCons()
	{
		return cons;
	}
	public void setCons(List<String> cons)
	{
		this.cons = cons;
	}
	public List<Detail> getDetails()
	{
		return details;
	}
	public void setDetails(List<Detail> details)
	{
		this.details = details;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}

	@Override
	public int getIconResId()
	{
		String t = getType();
		if(t == null)
			return R.drawable.ico_beer;
		else
			t = t.toLowerCase();
		
		if(t.equals("hospoda"))
			return R.drawable.ico_beer;
		else if(t.equals("bar"))
			return R.drawable.ico_drink;
		else if(t.equals("kavárna"))
			return R.drawable.ico_cafe;
		else if(t.equals("café"))
			return R.drawable.ico_cafe;
		else if(t.equals("restaurace"))
			return R.drawable.ico_restaurant;
		else if(t.equals("pizzerie"))
			return R.drawable.ico_pizza;
		else if(t.equals("fastfood"))
			return R.drawable.ico_fastfood;
		else if(t.equals("club"))
			return R.drawable.ico_music;
		else if(t.equals("zahrádka"))
			return R.drawable.ico_patio;
		else
			return R.drawable.ico_search;
	}
	
	public String getTitle()
	{
		return name;
	}
	
	public String getContact()
	{
		return contact;
	}
	public void setContact(String contact)
	{
		this.contact = contact;
	}
	public int getRating()
	{
		return rating;
	}
	public void setRating(int rating)
	{
		this.rating = rating;
	}
	
	@Override
	public String toString()
	{
		String result = name + "\n";
		if(street != null)
			result += street + "\n";
		if(city != null)
			result += city + "\n";
		if(contact != null)
			result += contact + "\n";
		if(web != null)
			result += web;
		return result;
	}
	
	private transient List<MapItemDetailItem> mDataForAdapter;
	
	public List<MapItemDetailItem> getDataForAdapter()
	{
		if(mDataForAdapter == null)
		{
			mDataForAdapter = new ArrayList<MapItemDetailItem>();
			if(pros != null)
			{
				for(String pro : pros)
					mDataForAdapter.add(new MapItemDetailItem(pro, MapItemDetailItem.TYPE_PRO));
			}
			if(cons != null)
			{
				for(String con : cons)
					mDataForAdapter.add(new MapItemDetailItem(con, MapItemDetailItem.TYPE_CON));
			}
			if(details != null)
			{
				for(Detail d : details)
					mDataForAdapter.add(new MapItemDetailItem(d));
			}
		}
		return mDataForAdapter;
	}
}