package com.scurab.android.myplaces.adapter;

import java.util.List;

import com.scurab.android.myplaces.R;
import com.scurab.android.myplaces.datamodel.Detail;
import com.scurab.android.myplaces.datamodel.MapItem;
import com.scurab.android.myplaces.datamodel.MapItemDetailItem;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailAdapter extends ArrayAdapter<MapItemDetailItem>
{
	private List<MapItemDetailItem> mData;
	private Resources mResources;
	
	public DetailAdapter(Context context, List<MapItemDetailItem> data)
	{
		super(context, 0, data);
		if(data == null)
			throw new NullPointerException("Data can't be null!");
		mData = data;
		mResources= context.getResources();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return updateView(position, convertView);
	}
	
	private View updateView(int position, View convertView)
	{
		DetailTag dt = null;
		if(convertView == null)
		{
			convertView = View.inflate(getContext(), R.layout.detail_item, null);
			dt = new DetailTag();
			dt.Title = (TextView) convertView.findViewById(R.id.tvMainText);
			dt.Detail = (TextView) convertView.findViewById(R.id.tvDescription);
			dt.Icon = (ImageView) convertView.findViewById(R.id.ivIcon);
			convertView.setTag(dt);
		}
		else
			 dt = (DetailTag) convertView.getTag();
		
		MapItemDetailItem o = mData.get(position);
		int type = o.getType();
		if(o.getType() == MapItemDetailItem.TYPE_DETAIL)
		{
			Detail d = o.getDetailValue();
			dt.Title.setText(d.getWhat());			
			dt.Detail.setText(d.getDetail());
			dt.Detail.setVisibility(View.VISIBLE);
			dt.Icon.setImageDrawable(mResources.getDrawable(R.drawable.ico_pencil));
		}
		else
		{
			dt.Title.setText(o.getValue());
			dt.Detail.setVisibility(View.GONE);
			if(type == MapItemDetailItem.TYPE_PRO)
				dt.Icon.setImageDrawable(mResources.getDrawable(R.drawable.ico_plus));
			else if(type == MapItemDetailItem.TYPE_CON)
				dt.Icon.setImageDrawable(mResources.getDrawable(R.drawable.ico_minus));
		}
		return convertView;
	}
	
	private class DetailTag
	{
		private TextView Title;
		private TextView Detail;
		private ImageView Icon;
	}
}
