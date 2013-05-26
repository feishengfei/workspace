package com.navchina.xzhou.nio;

public class Header
{
	private long len;
	private long type;
	private long sid;

	private int version;
	private int ce;
	private int status;
	
	public Header()
	{
		super();
	}

	public Header (byte[] buf)
	{
		super();
		Decoder dec = new Decoder(buf);
		len = dec.getLong(4);
		type = dec.getLong(4);
		sid = dec.getLong(4);
		
		version = dec.getInt(1);
		ce = dec.getInt(1);
		status = dec.getInt(1);
	}

	public long getLen()
	{
		return len;
	}

	public void setLen(long len)
	{
		this.len = len;
	}

	public long getType()
	{
		return type;
	}

	public void setType(long type)
	{
		this.type = type;
	}

	public long getSid()
	{
		return sid;
	}

	public void setSid(long sid)
	{
		this.sid = sid;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public int getCe()
	{
		return ce;
	}

	public void setCe(int ce)
	{
		this.ce = ce;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
	
	@Override
	public String toString()
	{
		return "len: " + len + "\n"
			+ "type: " + type + "\n"
			+ "sid: " + sid + "\n"
			+ "version: " + version + "\n"
			+ "ce: " + ce + "\n"
			+ "status: " + status + "\n";
	}
	
	public static void main(String[] args)
	{
		byte[] head_buf = new byte[]{
			0x00,
			0x00,
			0x00,
			0x10,
			0x02,
			0x01,
			0x00,
			0x01,
		};
		
		Header head = new Header(head_buf);
		System.out.println(head);
	}
}
