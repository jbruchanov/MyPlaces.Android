package com.scurab.android.myplaces.activity;

import com.scurab.android.myplaces.M;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.fragment.MapItemDetailFragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import junit.framework.TestCase;

public class MapItemDetailFragmentTest extends ActivityInstrumentationTestCase2<MapItemActivity>
{

	public MapItemDetailFragmentTest()
	{
		super(MapItemActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}
	
	public void setActivityIntent()
	{
		MapItem mi = getTestMapItem();
		Intent i = new Intent();
		i.putExtra(M.Constants.MAP_ITEM, mi);
		super.setActivityIntent(i);
	}
	
	private MapItem getTestMapItem()
	{
		MapItem mi = new MapItem();
		mi.setAuthor("Author01");
		mi.setCity("City01");
		mi.setCountry("Counter01");
		mi.setName("Name01");
		mi.setRating(7);
		mi.setContact("Contact01");
		mi.setStreet("Street01");
		mi.setType("Hospoda");		
		mi.setWeb("www.test.cz");		
		return mi;
	}
	
	public void testFindControls()
	{
		MapItemActivity a = getActivity();
		
		a.selectTab(0);
		MapItemDetailFragment f = (MapItemDetailFragment) a.getCurrentFragment(); 		
		
		assertNotNull(f.getNameEditText());
		assertEquals(R.id.etName, f.getNameEditText().getId());
		
		assertNotNull(f.getStreetEditText());
		assertEquals(R.id.etStreet, f.getStreetEditText().getId());
		
		assertNotNull(f.getCityEditText());
		assertEquals(R.id.etCity, f.getCityEditText().getId());
		
		assertNotNull(f.getCountryEditText());
		assertEquals(R.id.etCountry, f.getCountryEditText().getId());
		
		assertNotNull(f.getTypeSpinner());
		assertEquals(R.id.spType, f.getTypeSpinner().getId());
		
		assertNotNull(f.getContactEditText());
		assertEquals(R.id.etContact, f.getContactEditText().getId());
		
		assertNotNull(f.getWeblinkEditText());
		assertEquals(R.id.etWebLink, f.getWeblinkEditText().getId());
		
		assertNotNull(f.getAuthorEditText());
		assertEquals(R.id.etAuthor, f.getAuthorEditText().getId());
		
		assertNotNull(f.getRatingBar());
		assertEquals(R.id.ratingBar, f.getRatingBar().getId());
	}
	
	public void testLoadIntent()
	{
		setActivityIntent();
		MapItemActivity a = getActivity();
		a.selectTab(0);
		MapItemDetailFragment f = (MapItemDetailFragment) a.getCurrentFragment();
		
		MapItem mi =  (MapItem) a.getIntent().getExtras().get(M.Constants.MAP_ITEM);
		assertNotNull(mi);
		
		assertEquals(mi.getName(),f.getNameEditText().getText().toString());
		assertEquals(mi.getStreet(),f.getStreetEditText().getText().toString());
		assertEquals(mi.getCity(),f.getCityEditText().getText().toString());
		assertEquals(mi.getCountry(),f.getCountryEditText().getText().toString());
		//assertEquals(mi.getType(),f.getCountryEditText().getText().toString());
		assertEquals(mi.getContact(),f.getContactEditText().getText().toString());
		assertEquals(mi.getWeb(),f.getWeblinkEditText().getText().toString());
		assertEquals(mi.getAuthor(),f.getAuthorEditText().getText().toString());
		assertEquals(mi.getRating(),(int)(f.getRatingBar().getRating()*2f));
	}

}
