package com.navchina.bdi.pojo;

public class BDMessage
{
	private int nchars;
	private byte[] buf;
	
	public BDMessage()
	{
		super();
		nchars = 0;
		buf = null;
	}

	public int getNchars()
	{
		return nchars;
	}

	public void setNchars(int nchars)
	{
		this.nchars = nchars;
	}

	public byte[] getBuf()
	{
		return buf;
	}

	public void setBuf(byte[] buf)
	{
		this.buf = buf;
	}
	
	public void setBuf(String msg)
	{
		this.buf = msg.getBytes();
		this.nchars = buf.length;
	}
	
	@Override
	public String toString()
	{
		return new String(buf);
	}

}
