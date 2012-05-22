package com.scurab.android.myplaces.server;

import java.io.IOException;

import android.text.Html;
import com.scurab.android.myplaces.server.ServerConnection;

public class MockServerConnection extends ServerConnection
{
	public volatile int downloadCount = 0;

	public MockServerConnection(String serverAddress)
	{
		super(serverAddress);
	}

	@Override
	protected String downloadStars()
	{
		if (downloadCount == 0)
			return "[]";
		else if (downloadCount == 1)
			return "[{\"id\":10148397363569736,\"type\":\"20\",\"x\":14.500043,\"y\":50.13604}]";
		else
			return "[{\"id\":10148397363569736,\"type\":\"20\",\"x\":14.500043,\"y\":50.13604},"
					+ "{\"id\":1523660211492950,\"note\":\"adfa\",\"type\":\"13\",\"x\":14.505601,\"y\":50.135352},"
					+ "{\"id\":1523665030081294,\"type\":\"12\",\"x\":14.510837,\"y\":50.128777}]";
	}

	@Override
	protected String downloadMapItems(double left, double top, double bottom, double right) throws IOException
	{
		if (downloadCount == 0)
			return "[]";
		else if (downloadCount == 1)
			return Html
					.fromHtml(
							"[{\"id\":9972992318581640,\"type\":\"Hospoda\",\"name\":\"Dem\u00ednka\",\"country\":\"CZ\",\"city\":\"Prague\",\"street\":\"B\u011blehradsk\u00e1\","
									+ "\"web\":\"www.deminka.com\",\"streetViewLink\":\"X\\u003d14.431627;Y\\u003d50.077044;YAW\\u003d308.41;PITCH\\u003d-5.73;ZOOM\\u003d1\","
									+ "\"contact\":\"224 224 915\",\"x\":14.431647,\"y\":50.077035,\"rating\":8}]").toString();
		else
			return Html
					.fromHtml(
							"[{\"id\":9972992318581640,\"type\":\"Hospoda\",\"name\":\"Dem\u00ednka\",\"country\":\"CZ\",\"city\":\"Prague\",\"street\":\"B\u011blehradsk\u00e1\",\"web\":\"www.deminka.com\",\"streetViewLink\":\"X\\u003d14.431627;Y\\u003d50.077044;YAW\\u003d308.41;PITCH\\u003d-5.73;ZOOM\\u003d1\",\"contact\":\"224 224 915\",\"x\":14.431647,\"y\":50.077035,\"rating\":8},"
									+ "{\"id\":9973128346314856,\"type\":\"Hospoda\",\"name\":\"Legenda\",\"country\":\"CZ\",\"city\":\"Prague\",\"street\":\"Legerova\",\"web\":\"www.ilegenda.cz\",\"streetViewLink\":\"X\\u003d14.430375;Y\\u003d50.073803;YAW\\u003d278.3;PITCH\\u003d-3.65;ZOOM\\u003d0\",\"contact\":\"(+420) 737 626 848, (+420) 296 180 310\",\"x\":14.430333,\"y\":50.073795,\"rating\":4},"
									+ "{\"id\":9973988218395496,\"type\":\"Restaurace\",\"name\":\"U Z\u00e1bransk\u00fdch\",\"country\":\"CZ\",\"city\":\"Prague\",\"street\":\"K\u0159\u00ed\u017ekova 330\",\"web\":\"www.uzabranskych.cz\",\"streetViewLink\":\"X\\u003d14.453203;Y\\u003d50.09298;YAW\\u003d0;PITCH\\u003d5;ZOOM\\u003d0\",\"author\":\"Luc\",\"x\":14.45318,\"y\":50.09304,\"rating\":0}]")
					.toString();
	}

}