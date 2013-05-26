package com.cookbook.layout_widgets.list;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Msg
{
	private boolean isChecked;
	private String contacts;
	private long ts;
	private boolean isUnread;
	private String content;

	@Override
	public String toString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date(ts * 1000));
		return "[" + time + "]from " + contacts + ": " + content;
	}

	public void toggleChecked()
	{
		isChecked = !isChecked;
	}

	// getter & setters
	public boolean isChecked()
	{
		return isChecked;
	}

	public void setChecked(boolean isChecked)
	{
		this.isChecked = isChecked;
	}

	public String getContacts()
	{
		return contacts;
	}

	public void setContacts(String contacts)
	{
		this.contacts = contacts;
	}

	public long getTs()
	{
		return ts;
	}

	public void setTs(long ts)
	{
		this.ts = ts;
	}

	public boolean isUnread()
	{
		return isUnread;
	}

	public void setUnread(boolean isUnread)
	{
		this.isUnread = isUnread;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

}
