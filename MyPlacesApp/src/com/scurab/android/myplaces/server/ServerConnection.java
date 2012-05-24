package com.scurab.android.myplaces.server;

import java.io.IOException;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
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
	private final static Gson sGson = new Gson();
	
	private final String mServerUrl;
	private final String mStarsUrl;
	private final String mMapItemTemplateUrl;
	private final String mMapItemsTemplateUrl;
	
	private final String STARS_SUFFIX = "/stars";
	private final String MAPITEM_SUFFIX = "/mapitems/{%s}";
	private final String MAPITEMS_SUFFIX = "/mapitems/%s/%s/%s/%s";
	
	/**
	 * Create connection provider to server
	 * @param serverAddress like "http://myplaces.myserver.com:8183"
	 * 
	 */
	public ServerConnection(String serverAddress)
	{
		mServerUrl = serverAddress;
		mStarsUrl = mServerUrl + STARS_SUFFIX;
		mMapItemTemplateUrl = mServerUrl + MAPITEM_SUFFIX;
		mMapItemsTemplateUrl = mServerUrl + MAPITEMS_SUFFIX;
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
	
	public void save(Star s)
	{
		ClientResource resource = getClientResource(mStarsUrl);
		String value = sGson.toJson(s);
		if(s.getId() == 0)
			resource.post(value, MediaType.APPLICATION_JSON);
		else
			resource.put(value, MediaType.APPLICATION_JSON);
		Response r = resource.getResponse();
		assert(r.getStatus().isSuccess());
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
}
