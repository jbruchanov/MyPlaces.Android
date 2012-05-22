package com.scurab.android.myplaces.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.Star;

import junit.framework.TestCase;

public class ServerConnectionTest extends TestCase
{
	private String serverUrl = "http://myplaces.scurab.com:8182";
			
	public void testGetStarsOne() throws IOException
	{
		MockServerConnection ms = new MockServerConnection(serverUrl);
		ms.downloadCount = 1;
		List<Star> result = Arrays.asList(ms.getStars());
		assertEquals(1,result.size());
		
		Star s = result.get(0);
		assertEquals(10148397363569736L, s.getId());
		assertEquals("20", s.getType());
		assertEquals(14.500043, s.getX());
		assertEquals(50.13604, s.getY());
	}
	
	public void testGetStarsNone() throws IOException
	{
		MockServerConnection ms = new MockServerConnection(serverUrl);
		Star[] result = ms.getStars();
		assertNotNull(result);
		assertEquals(0,result.length);
	}
	
	public void testGetStarsMore() throws IOException
	{
		MockServerConnection ms = new MockServerConnection(serverUrl);
		ms.downloadCount = 3;
		Star[] result = ms.getStars();
		assertNotNull(result);
		assertEquals(3,result.length);
		
		assertEquals(10148397363569736L,result[0].getId());
		assertEquals("20",result[0].getType());
		
		assertEquals(1523660211492950L,result[1].getId());
		assertEquals("13",result[1].getType());
		assertEquals("adfa",result[1].getTitle());
		
		assertEquals(1523665030081294L,result[2].getId());
		assertEquals("12",result[2].getType());
	}

	public void testGetStarsFromRealServer() throws IOException
	{
		ServerConnection ms = new ServerConnection(serverUrl);
		Star[] result = ms.getStars();
		assertNotNull(result);
		assertTrue(result.length > 0);
		for(Star mi : result)
		{
			assertTrue(mi.getType().length() > 0);
		}
	}
	
	
	public void testGetMapItemsNone() throws IOException
	{
		MockServerConnection ms = new MockServerConnection(serverUrl);	
		MapItem[] result = ms.getMapItems(0,0,0,0);
		
		assertNotNull(result);
		assertEquals(0,result.length);
	}
	
	public void testGetMapItemsOne() throws IOException
	{
		MockServerConnection ms = new MockServerConnection(serverUrl);
		ms.downloadCount = 1;
		MapItem[] result = ms.getMapItems(0,0,0,0);
		
		assertNotNull(result);
		assertEquals(1,result.length);
		MapItem mi = result[0];
		
		assertEquals(9972992318581640L,mi.getId());
		assertEquals("Hospoda",mi.getType());
		assertEquals("Demínka",mi.getTitle());
		assertEquals("CZ",mi.getCountry());
		assertEquals("Prague",mi.getCity());
		assertEquals("Bělehradská",mi.getStreet());
		assertEquals("www.deminka.com",mi.getWeb());
		assertTrue(mi.getStreetViewLink().length() > 0);
		assertEquals("224 224 915",mi.getContact());
		assertEquals(14.431647,mi.getX());
		assertEquals(50.077035,mi.getY());
		assertEquals(8,mi.getRating());
	}
	
	public void testGetMapItemsMore() throws IOException
	{
		MockServerConnection ms = new MockServerConnection(serverUrl);
		ms.downloadCount = 3;
		MapItem[] result = ms.getMapItems(0,0,0,0);
		
		assertNotNull(result);
		assertEquals(3,result.length);
		for(MapItem mi : result)
		{
			assertTrue(mi.getTitle().length() > 0);
		}
	}

	public void testGetMapItemsFromRealServer() throws IOException
	{
		ServerConnection ms = new ServerConnection(serverUrl);
		MapItem[] result = ms.getMapItems(14.5, 51, 14.6, 50);
		assertNotNull(result);
		assertTrue(result.length > 0);
		for(MapItem mi : result)
		{
			assertTrue(mi.getTitle().length() > 0);
		}
	}
}
