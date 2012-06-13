package com.scurab.android.myplaces;

public class M
{
	public final static double COORD_HELP_MAPPER = 1E6;
	
	public static final class Defaults
	{

		public static final String PROPERTY_SERVER_URL = "http://myplaces.scurab.com:8182";
//		public static final String PROPERTY_SERVER_URL = "http://192.168.100.11:8182";
		
	}
	
	public static final class Constants
	{
		public static final int DIALOG_EDITTEXT_ID = 0x91237489;
		public static final String MAP_ITEM = "MAP_ITEM";
		
		public static final int RESULT_ADD = 0xDD;
		public static final int RESULT_DELETE = 0xDE;
		public static final int RESULT_UPDATE = 0xDF;
		public static final String NEW_MAP_ITEM = "NEW_MAP_ITEM";
		public static final int REQUEST_EDIT_MAP_ITEM = 0xEAE;
	}

	private M(){}	
}
