package com.scurab.android.myplaces.widget.dialog;

import java.util.List;

import com.scurab.android.myplaces.util.AppUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Address;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListDialog<T> extends AlertDialog
{
	public interface OnItemSelectListener<T>
	{
		public void onItemClick(T t);
	}
	
	private Context mContext;
	private ArrayAdapter<T> mAdapter;
	private List<T> mData;
	private OnItemSelectListener<T> mListener;
	
	public ListDialog(Context context, List<T> data)
	{
		super(context);
		mContext = context;
		mData = data;
		build();		
	}
	
	public ListDialog(Context context, List<T> data, ArrayAdapter<T> adapter)
	{
		super(context);
		mContext = context;
		mData = data;
		mAdapter = adapter;
		build();		
	}
	
	private void build()
	{	
		if(mAdapter == null)
			mAdapter = new ArrayAdapter<T>(mContext, android.R.layout.simple_expandable_list_item_1, mData);
		ListView lv = new ListView(getContext());
		lv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				ListDialog.this.onItemClick(mData.get(position), position);
				dismiss();
			}
		});
		setView(lv);
	}
	
	public void onItemClick(T t, int position)
	{
		if(mListener != null)
			mListener.onItemClick(t);
	}

	public void setOnItemClickListener(OnItemSelectListener<T> listener)
	{
		mListener = listener;
	}	
	
	public static class AddressAdapter extends ArrayAdapter<Address>
	{
		private List<Address> mData;
		public AddressAdapter(Context context, List<Address> data)
		{
			super(context, 0, data);
			mData = data;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			return getOrCreateView(position,(ViewGroup)convertView);
		}
		
		private View getOrCreateView(int position, ViewGroup convertView)
		{
			if(convertView == null)
			{
				Context c = getContext();
				LinearLayout ll = new LinearLayout(c);
				ll.setPadding(5, 5, 5, 5);
				ll.setOrientation(LinearLayout.VERTICAL);
				Tag t = new Tag();
				
				
				t.Street = new TextView(c);
				t.Street.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
				t.Street.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				
				t.City = new TextView(c);
				t.City.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
				t.City.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				
				t.Country = new TextView(c);
				t.Country.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);
				t.Country.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				
				
				ll.addView(t.Street);
				ll.addView(t.City);
				ll.addView(t.Country);
				
				ll.setTag(t);
				
				convertView = ll;
				
			}
			
			Tag t = (Tag) convertView.getTag();
			Address a = mData.get(position);
			t.Street.setText(AppUtils.emptyIfNull(a.getThoroughfare()));
			t.City.setText(AppUtils.emptyIfNull(a.getAdminArea()));
			t.Country.setText(AppUtils.emptyIfNull(a.getCountryName()));
			return convertView;
		}
		
		private static class Tag
		{
			TextView Street;
			TextView City;
			TextView Country;
		}
	}
}
