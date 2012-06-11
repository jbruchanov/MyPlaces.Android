package com.scurab.android.myplaces.fragment;

import com.scurab.android.myplaces.M;
import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.MapItem;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

public class MapItemDetailFragment extends Fragment
{
	private View mContentView;
	private EditText mName;
	private EditText mStreet;
	private EditText mCity;
	private EditText mCountry;
	private Spinner mType;
	private EditText mContact;
	private EditText mWeblink;
	private EditText mAuthor;
	private RatingBar mRatingBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if(mContentView == null)
		{
			mContentView = inflater.inflate(R.layout.mapitem_detail,null);
			init(getActivity().getIntent());			
		}
		return mContentView;
	}
	
	private void init(Intent i)
	{
		mName = (EditText)mContentView.findViewById(R.id.etName);		
		mStreet= (EditText)mContentView.findViewById(R.id.etStreet);
		mCity = (EditText)mContentView.findViewById(R.id.etCity);
		mCountry = (EditText)mContentView.findViewById(R.id.etCountry);
		mType = (Spinner)mContentView.findViewById(R.id.spType);
		mContact = (EditText)mContentView.findViewById(R.id.etContact);
		mWeblink= (EditText)mContentView.findViewById(R.id.etWebLink);
		mAuthor = (EditText)mContentView.findViewById(R.id.etAuthor);
		mRatingBar = (RatingBar)mContentView.findViewById(R.id.ratingBar);		
		
//		mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){@Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser){mAuthor.setText(String.valueOf(rating));}});
		
		if(i.hasExtra(M.Constants.MAP_ITEM))
		{
			MapItem mi = (MapItem) i.getExtras().get(M.Constants.MAP_ITEM);
			if(mi != null)
				setMapItem(mi);
		}
	}
	
	public void setMapItem(MapItem mi)
	{
		mName.setText(mi.getName());
		mStreet.setText(mi.getStreet());
		mCity.setText(mi.getCity());
		mCountry.setText(mi.getCountry());
		mContact.setText(mi.getContact());
		mWeblink.setText(mi.getWeb());
		mAuthor.setText(mi.getAuthor());
		mRatingBar.setRating(mi.getRating()/2f);
	}

	public EditText getNameEditText()
	{
		return mName;
	}

	public EditText getStreetEditText()
	{
		return mStreet;
	}

	public EditText getCityEditText()
	{
		return mCity;
	}

	public EditText getCountryEditText()
	{
		return mCountry;
	}

	public Spinner getTypeSpinner()
	{
		return mType;
	}

	public EditText getContactEditText()
	{
		return mContact;
	}

	public EditText getWeblinkEditText()
	{
		return mWeblink;
	}

	public RatingBar getRatingBar()
	{
		return mRatingBar;
	}
	
	public EditText getAuthorEditText()
	{
		return mAuthor;
	}
	
}
