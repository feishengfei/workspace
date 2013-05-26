package com.navchina.sms.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.navchina.R;
import com.navchina.sms.model.SmsRoot;
import com.navchina.sms.view.SmsRootItem;

public class SmsRootAdapter extends BaseAdapter
{
	private ArrayList<SmsRoot> roots;
	private Context context;

	public SmsRootAdapter(ArrayList<SmsRoot> roots, Context context)
	{
		super();
		this.roots = roots;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return roots.size();
	}

	@Override
	public Object getItem(int position)
	{
		return (null == roots) ? null : roots.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SmsRootItem sri;
		if(null == convertView){
			sri = (SmsRootItem)View.inflate(context, R.layout.sms_root_item, null);
		}
		else {
			sri = (SmsRootItem)convertView;
		}
		sri.setRoot(roots.get(position));
		return sri;
	}

}
