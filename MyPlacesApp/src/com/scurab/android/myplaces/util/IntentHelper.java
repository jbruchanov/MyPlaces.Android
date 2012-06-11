package com.scurab.android.myplaces.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentHelper
{
	public IntentHelper(){}
	
	public static void openWebLink(Context context, String url)
	{		
		if(!url.startsWith("http"))
			url = "http://" + url;
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(i);
	}
	
	public static void sendText(Context context, String text)
	{
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("plain/text");
		i.putExtra(Intent.EXTRA_TEXT, text);
		context.startActivity(i);
	}

	public static void callNumber(Context context, String number)
	{
		Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + fixPhoneNumber(number)));
		i.putExtra(Intent.EXTRA_PHONE_NUMBER, number);
		context.startActivity(i);
	}
	
	public static String fixPhoneNumber(String number)
	{
//		StringBuilder sb = new StringBuilder();
		return number.replace(" ", "");
	}
}
