package com.cookbook.layout_widgets.list;

import java.util.ArrayList;
import java.util.Iterator;

import com.cookbook.launch_activity.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class MsgAdapter extends BaseAdapter
{
	private ArrayList<Msg> msgs;
	private Context context;

	public MsgAdapter(ArrayList<Msg> msgs, Context context)
	{
		super();
		this.msgs = msgs;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return msgs.size();
	}

	@Override
	public Object getItem(int position)
	{
		return (null == msgs) ? null : msgs.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		MsgItem mi;
		if (null == convertView) {
			mi = (MsgItem) View.inflate(context, R.layout.message_item, null);
		}
		else {
			mi = (MsgItem) convertView;
		}
		mi.setMsg(msgs.get(position));
		return mi;
	}

	public void deleteCheckedMsg()
	{
		Iterator<Msg> iter = msgs.iterator();
		int i = 0;
		while (iter.hasNext()) {
			Msg msg = (Msg) iter.next();
			if (msg.isChecked()) {
				iter.remove();
			}
			i++;
		}
		notifyDataSetChanged();
	}

	/*
	public void setChecked(int position, boolean isChecked)
	{
		Msg msg = (Msg) getItem(position);
		msg.setChecked(isChecked);
		notifyDataSetChanged();
	}
	*/

	public void checkAll(boolean isChecked)
	{
		Iterator<Msg> iter = msgs.iterator();
		while (iter.hasNext()) {
			Msg msg = (Msg) iter.next();
			msg.setChecked(isChecked);
		}
		notifyDataSetChanged();
	}

	/*
	public void toggleChecked(int position)
	{
		Msg msg = (Msg) getItem(position);
		msg.toggleChecked();
		notifyDataSetChanged();
	}
	*/

	public int getCheckedCount()
	{
		int ret = 0;
		Iterator<Msg> iter = msgs.iterator();
		while (iter.hasNext()) {
			Msg msg = (Msg) iter.next();
			if (msg.isChecked())
				ret++;
		}
		return ret;
	}

}
