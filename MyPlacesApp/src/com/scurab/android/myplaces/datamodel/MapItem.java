package com.scurab.android.myplaces.datamodel;

import java.io.Serializable;
import java.util.List;

import com.scurab.android.myplaces.interfaces.HasDescription;
import com.scurab.android.myplaces.util.AppUtils;

public class MapItem implements Serializable , HasDescription
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
	public double getX()
	{
		return x;
	}
	public void setX(double x)
	{
		this.x = AppUtils.round(x,6);
	}
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

//	public String getIconUrl()
//	{
//		String t = getType();
//		if(t == null)
//			return AppConstants.MediumIcons.ICO_BEER;
//		else
//			t = t.toLowerCase();
//		
//		if(t.equals("hospoda"))
//			return AppConstants.MediumIcons.ICO_BEER;
//		else if(t.equals("bar"))
//			return AppConstants.MediumIcons.ICO_DRINK;
//		else if(t.equals("kav�rna"))
//			return AppConstants.MediumIcons.ICO_CAFE;
//		else if(t.equals("caf�"))
//			return AppConstants.MediumIcons.ICO_CAFE;
//		else if(t.equals("restaurace"))
//			return AppConstants.MediumIcons.ICO_RESTAURANT;
//		else if(t.equals("pizzerie"))
//			return AppConstants.MediumIcons.ICO_PIZZA;
//		else if(t.equals("fastfood"))
//			return AppConstants.MediumIcons.ICO_FASTFOOD;
//		else if(t.equals("club"))
//			return AppConstants.MediumIcons.ICO_MUSIC;
//		else if(t.equals("zahr�dka"))
//			return AppConstants.MediumIcons.ICO_PATIO;
//		else
//			return AppConstants.MediumIcons.ICO_SEARCH;
//	}
	
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
	public String getDescription()
	{
		return getTitle() + " " + getType();
	}
}