package com.scurab.android.myplaces.widget;

import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.MapItem;

import android.test.AndroidTestCase;

public class MapItemPanelTest extends AndroidTestCase
{
	public void testFindingControls()
	{
		MapItemPanel mip = new MapItemPanel(mContext);		
		assertNotNull(mip.getNameTextView());
		assertEquals(R.id.tvName,mip.getNameTextView().getId());
		assertNotNull(mip.getStreetTextView());
		assertEquals(R.id.tvStreet,mip.getStreetTextView().getId());
		assertNotNull(mip.getContactTextView());
		assertEquals(R.id.tvContact,mip.getContactTextView().getId());
	}
	
	public void testVisiblityOnStart()
	{
		MapItemPanel mip = new MapItemPanel(mContext);
		assertFalse(mip.isVisible());
	}
	
	public void testVisiblityShow()
	{
		MapItemPanel mip = new MapItemPanel(mContext);
		mip.setAnimations(false);
		mip.show();
		assertTrue(mip.isVisible());
	}
	
	public void testVisiblityHide()
	{
		MapItemPanel mip = new MapItemPanel(mContext);
		mip.setAnimations(false);
		mip.show();
		mip.hide();
		assertFalse(mip.isVisible());
		assertNull(mip.getMapItem());
	}
	
	public void testSetItem()
	{
		String name = "TestName01";
		String street = "TestStreet01";
		String phone = "TestPhone01";
		MapItem mi = new MapItem();
		mi.setName(name);
		mi.setStreet(street);
		mi.setContact(phone);
		
		MapItemPanel mip = new MapItemPanel(mContext);
		mip.setMapItem(mi);
		
		assertEquals(name,mip.getNameTextView().getText().toString());
		assertEquals(street,mip.getStreetTextView().getText().toString());
		assertEquals(phone,mip.getContactTextView().getText().toString());
	}
	
	public void testItemWeb()
	{
		MapItem mi = new MapItem();
		mi.setWeb("");
		MapItemPanel mip = new MapItemPanel(mContext);
		mip.setMapItem(mi);
		assertFalse(mip.findViewById(R.id.ibWeb).isEnabled());
		
		mi.setWeb(null);
		mip.setMapItem(mi);
		assertFalse(mip.findViewById(R.id.ibWeb).isEnabled());
		
		mi.setWeb("www.abc.def");
		mip.setMapItem(mi);
		assertTrue(mip.findViewById(R.id.ibWeb).isEnabled());
	}
	
	public void testItemContact()
	{
		MapItem mi = new MapItem();
		mi.setContact("");
		MapItemPanel mip = new MapItemPanel(mContext);
		mip.setMapItem(mi);
		assertFalse(mip.findViewById(R.id.ibPhone).isEnabled());
		
		mi.setContact(null);
		mip.setMapItem(mi);
		assertFalse(mip.findViewById(R.id.ibPhone).isEnabled());
		
		mi.setContact("123456789");
		mip.setMapItem(mi);
		assertTrue(mip.findViewById(R.id.ibPhone).isEnabled());
	}
}
