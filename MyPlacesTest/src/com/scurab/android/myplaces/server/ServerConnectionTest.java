package com.scurab.android.myplaces.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.text.Html;

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
	
	private class MockServerConnection extends ServerConnection
	{
		public int downloadCount = 0;
		public MockServerConnection(String serverAddress)
		{
			super(serverAddress);
		}
		
		@Override
		protected String downloadStars()
		{		
			if(downloadCount == 0)
				return "[]";
			else if (downloadCount == 1)				
				return "[{\"id\":10148397363569736,\"type\":\"20\",\"x\":14.500043,\"y\":50.13604}]";
			else
				return "[{\"id\":10148397363569736,\"type\":\"20\",\"x\":14.500043,\"y\":50.13604}," +
						"{\"id\":1523660211492950,\"note\":\"adfa\",\"type\":\"13\",\"x\":14.505601,\"y\":50.135352}," +
						"{\"id\":1523665030081294,\"type\":\"12\",\"x\":14.510837,\"y\":50.128777}]";
		}
		
		@Override
		protected String downloadMapItems(double left, double top, double bottom, double right) throws IOException
		{
			if(downloadCount == 0)
				return "[]";
			else if(downloadCount == 1)
				return Html.fromHtml("[{\"id\":9972992318581640,\"type\":\"Hospoda\",\"name\":\"Dem\u00ednka\",\"country\":\"CZ\",\"city\":\"Prague\",\"street\":\"B\u011blehradsk\u00e1\"," +
						"\"web\":\"www.deminka.com\",\"streetViewLink\":\"X\\u003d14.431627;Y\\u003d50.077044;YAW\\u003d308.41;PITCH\\u003d-5.73;ZOOM\\u003d1\"," +
						"\"contact\":\"224 224 915\",\"x\":14.431647,\"y\":50.077035,\"rating\":8}]").toString();
			else
				return Html.fromHtml("[{\"id\":9972992318581640,\"type\":\"Hospoda\",\"name\":\"Dem\u00ednka\",\"country\":\"CZ\",\"city\":\"Prague\",\"street\":\"B\u011blehradsk\u00e1\",\"web\":\"www.deminka.com\",\"streetViewLink\":\"X\\u003d14.431627;Y\\u003d50.077044;YAW\\u003d308.41;PITCH\\u003d-5.73;ZOOM\\u003d1\",\"contact\":\"224 224 915\",\"x\":14.431647,\"y\":50.077035,\"rating\":8}," +
						"{\"id\":9973128346314856,\"type\":\"Hospoda\",\"name\":\"Legenda\",\"country\":\"CZ\",\"city\":\"Prague\",\"street\":\"Legerova\",\"web\":\"www.ilegenda.cz\",\"streetViewLink\":\"X\\u003d14.430375;Y\\u003d50.073803;YAW\\u003d278.3;PITCH\\u003d-3.65;ZOOM\\u003d0\",\"contact\":\"(+420) 737 626 848, (+420) 296 180 310\",\"x\":14.430333,\"y\":50.073795,\"rating\":4}," +
						"{\"id\":9973988218395496,\"type\":\"Restaurace\",\"name\":\"U Z\u00e1bransk\u00fdch\",\"country\":\"CZ\",\"city\":\"Prague\",\"street\":\"K\u0159\u00ed\u017ekova 330\",\"web\":\"www.uzabranskych.cz\",\"streetViewLink\":\"X\\u003d14.453203;Y\\u003d50.09298;YAW\\u003d0;PITCH\\u003d5;ZOOM\\u003d0\",\"author\":\"Luc\",\"x\":14.45318,\"y\":50.09304,\"rating\":0}]").toString();
		}
		
	}
}
