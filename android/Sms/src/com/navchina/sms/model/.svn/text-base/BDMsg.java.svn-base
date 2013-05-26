package com.navchina.sms.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BDMsg
{
	private long id;
	private String num;
	private String content;
	private int type;
	private long time;
	private long serialnum;
	private int lon;
	private int lat;
	private boolean isPoint;
	private boolean isChecked;

	public BDMsg(String num, String content, int type, long time,
			long serialnum, boolean isPoint)
	{
		super();
		this.num = num;
		this.content = content;
		this.type = type;
		this.time = time;
		this.serialnum = serialnum;
		this.isPoint = isPoint;
	}

	@Override
	public String toString()
	{
		String time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = sdf.format(new Date(this.time * 1000));
		return "[" + num + ":" + time + "/" + serialnum + "]" + "\n" + content;
	}

	// getter & setter
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getNum()
	{
		return num;
	}

	public void setNum(String num)
	{
		this.num = num;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public long getSerialnum()
	{
		return serialnum;
	}

	public void setSerialnum(long serialnum)
	{
		this.serialnum = serialnum;
	}

	public int getLon()
	{
		return lon;
	}

	public void setLon(int lon)
	{
		this.lon = lon;
	}

	public int getLat()
	{
		return lat;
	}

	public void setLat(int lat)
	{
		this.lat = lat;
	}

	public boolean isPoint()
	{
		return isPoint;
	}

	public void setPoint(boolean isPoint)
	{
		this.isPoint = isPoint;
	}

	public void setChecked(boolean isChecked)
	{
		this.isChecked = isChecked;
	}

	public boolean isChecked()
	{
		return isChecked;
	}

}
