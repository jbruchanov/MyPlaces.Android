package com.scurab.android.myplaces.server;

import java.io.IOException;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.Star;

/**
 * 
 * @author Joe Scurab
 *
 * Base class for communication with server
 */
public class ServerConnection
{
	private final static Gson sGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
	
	private final String mServerUrl;
	private final String mStarsUrl;
	private final String mMapItemTypesUrl;
	private final String mMapItemTemplateUrl;
	private final String mMapItemsUrl;
	private final String mMapItemsTemplateUrl;
	
	private final String STARS_SUFFIX = "/stars";	
	private final String STARS_DELETE_SUFFIX = "/stars/%s";
	private final String MAPITEM_SUFFIX = "/mapitems/%s";
	private final String MAPITEMS_SUFFIX = "/mapitems";
	private final String MAPITEMS_COORDS_SUFFIX = "/mapitems/%s/%s/%s/%s";
	private final String MAPITEM_TYPES_SUFFIX = "/mapitemtypes";
	
	/**
	 * Create connection provider to server
	 * @param serverAddress like "http://myplaces.myserver.com:8183"
	 * 
	 */
	public ServerConnection(String serverAddress)
	{
		if(serverAddress == null || "".equals(serverAddress))
			serverAddress = "http://10.1.1.11";
		mServerUrl = serverAddress;
		mStarsUrl = mServerUrl + STARS_SUFFIX;
		mMapItemTemplateUrl = mServerUrl + MAPITEM_SUFFIX;
		mMapItemsTemplateUrl = mServerUrl + MAPITEMS_COORDS_SUFFIX;
		mMapItemsUrl = mServerUrl + MAPITEMS_SUFFIX;
		mMapItemTypesUrl = mServerUrl + MAPITEM_TYPES_SUFFIX;
	}
	
	public String getServerUrl()
	{
		return mServerUrl;
	}
	
	public Star[] getStars() throws IOException
	{
		String response = downloadStars();
		@SuppressWarnings("unchecked")
		Star[] result = sGson.fromJson(response, Star[].class);
		if(result == null) result = new Star[]{};
		return result;
	}
	
	public MapItem[] getMapItems(double left, double top, double right, double bottom) throws IOException
	{
		String response = downloadMapItems(left, top, right, bottom);
		@SuppressWarnings("unchecked")
		MapItem[] result = sGson.fromJson(response, MapItem[].class);
		if(result == null) result = new MapItem[]{};
		return result;
	}
	
	public String[] getMapItemTypes() throws ResourceException, IOException
	{
		ClientResource resource = getClientResource(mMapItemTypesUrl);		
		String result = resource.get().getText();
		return sGson.fromJson(result, String[].class);
	}
	
	public MapItem getDetailedMapItem(long id) throws IOException
	{
		String response = downloadMapItem(id);
		@SuppressWarnings("unchecked")
		MapItem[] result = sGson.fromJson(response, MapItem[].class);
		if(result != null && result.length == 1)
			return result[0];
		else
			return null;
	}
	
	public Star save(Star s)
	{
		ClientResource resource = getClientResource(mStarsUrl);
		String value = sGson.toJson(s);
		if(s.getId() == 0)
			resource.post(value, MediaType.APPLICATION_JSON);
		else
			resource.put(value, MediaType.APPLICATION_JSON);
		Response r = resource.getResponse();
		Star saved = sGson.fromJson(r.getEntityAsText(), Star.class);
		assert(r.getStatus().isSuccess());
		return saved;
	}
	
	protected String downloadStars() throws IOException
	{
		ClientResource resource = getClientResource(mStarsUrl);
		String response = resource.get().getText();
		return response;
	}
	
	protected String downloadMapItems(double left, double top, double right, double bottom) throws IOException
	{
		ClientResource resource = getClientResource(String.format(mMapItemsTemplateUrl,left,bottom,right,top));
		String response = resource.get().getText();
		return response;
	}
	
	protected String downloadMapItem(long id) throws IOException
	{
		ClientResource resource = getClientResource(String.format(mMapItemTemplateUrl,id));
		String response = resource.get().getText();
		return response;
	}
	
	protected ClientResource getClientResource(String url)
	{
		ClientResource resource = new ClientResource(url);			
		resource.setRetryOnError(false);
		Context context = new Context();
		context.getParameters().add("socketTimeout", "1000");
		Client client = new Client(context,Protocol.HTTP);
		client.setConnectTimeout(5000);
		resource.setNext(client);
		return resource;
	}

	public void delete(Star star)
	{
		String url = mServerUrl + String.format(STARS_DELETE_SUFFIX,star.getId());
		ClientResource resource = getClientResource(url);
		resource.delete();
		Response r = resource.getResponse();
		assert(r.getStatus().isSuccess());
	}

	public void delete(MapItem item)
	{
		String url = mServerUrl + String.format(MAPITEM_SUFFIX,item.getId());
		ClientResource resource = getClientResource(url);
		resource.delete();
		Response r = resource.getResponse();
		assert(r.getStatus().isSuccess());
	}

	public MapItem save(MapItem object)
	{
		ClientResource resource = getClientResource(mMapItemsUrl);
		String value = sGson.toJson(object);
		if(object.getId() == 0)
			resource.post(value, MediaType.APPLICATION_JSON);
		else
			resource.put(value, MediaType.APPLICATION_JSON);
		Response r = resource.getResponse();
		MapItem saved = sGson.fromJson(r.getEntityAsText(), MapItem.class);
		assert(r.getStatus().isSuccess());
		return saved;
	}
}
