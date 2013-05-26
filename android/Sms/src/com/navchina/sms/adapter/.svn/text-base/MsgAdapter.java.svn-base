package com.navchina.sms.adapter;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.navchina.R;
import com.navchina.sms.model.BDMsg;
import com.navchina.sms.view.MsgItem;

public class MsgAdapter extends BaseAdapter
{
	private ArrayList<BDMsg> msgs;
	private Context context;

	public MsgAdapter(ArrayList<BDMsg> msgs, Context context)
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

	public Long[] deleteCheckedMsg()
	{
		ArrayList<Long> checkedIds = new ArrayList<Long>();
		ArrayList<BDMsg> checkedMsgs = new ArrayList<BDMsg>();
		for (BDMsg msg : msgs) {
			if (msg.isChecked()) {
				checkedIds.add(msg.getId());
				checkedMsgs.add(msg);
			}
		}
		msgs.removeAll(checkedMsgs);
		notifyDataSetChanged();

		return checkedIds.toArray(new Long[] {});
	}

	public void checkAll(boolean isChecked)
	{
		Iterator<BDMsg> iter = msgs.iterator();
		while (iter.hasNext()) {
			BDMsg msg = (BDMsg) iter.next();
			msg.setChecked(isChecked);
		}
		notifyDataSetChanged();
	}

	public int getCheckedCount()
	{
		int ret = 0;
		Iterator<BDMsg> iter = msgs.iterator();
		while (iter.hasNext()) {
			BDMsg msg = (BDMsg) iter.next();
			if (msg.isChecked())
				ret++;
		}
		return ret;
	}

	public ArrayList<Long> getCheckedIds()
	{
		ArrayList<Long> ret = new ArrayList<Long>();
		Iterator<BDMsg> iter = msgs.iterator();
		while (iter.hasNext()) {
			BDMsg msg = (BDMsg) iter.next();
			if (msg.isChecked()) {
				ret.add(msg.getId());
			}
		}
		return ret;
	}

}
