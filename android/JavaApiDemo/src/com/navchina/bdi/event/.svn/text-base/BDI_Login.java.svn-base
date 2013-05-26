package com.navchina.bdi.event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import com.navchina.bdi.BDI_Util;

public class BDI_Login extends BDI_Event
{
	public static final int SEND_ONLY = 0;
	public static final int RECEIVE_ONLY = 1;
	public static final int SEND_RECEIVE = 2;

	private byte[] usr = null;
	private byte[] pwd = null;
	private long ts;
	private int mode;
	private int minVer;
	private int maxVer;

	public BDI_Login()
	{
		super(BDI_Event.LOGIN);
		mode = SEND_RECEIVE;
		ts = 0;
		minVer = 0x10;
		maxVer = 0x10;
		usr = new byte[16];
		pwd = new byte[20];
	}

	@Override
	final public byte[] encodeBody() throws IOException
	{
		ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
		DataOutputStream d_out = new DataOutputStream(ba_out);
		d_out.write(usr);
		d_out.write(pwd);
		d_out.writeInt((int)ts);
		d_out.writeByte(mode);
		d_out.writeByte(maxVer);
		d_out.writeByte(minVer);
		d_out.write(new byte[9]); // RESERVED
		return ba_out.toByteArray();
	}

	@Override
	public String toString()
	{
		String ret = "\t[" + BDI_Util.time2Str(ts) + "]" + mode + ", "
				+ Integer.toHexString(maxVer) + ", "
				+ Integer.toHexString(minVer) + "[" + new String(usr) + "@"
				+ new String(pwd) + "]\n";
		return fh_String() + "\n" + ret;
	}

	public void setUsr(String usr)
	{
		BDI_Util.copyStr2Bytes(usr, this.usr);
	}

	public void setPwd(String pwd)
	{
		BDI_Util.copyStr2Bytes(pwd, this.pwd);
	}

	public void setTs(long ts)
	{
		this.ts = ts;
	}

	public void setMode(int mode)
	{
		this.mode = mode;
	}

	public void setMinVer(int minVer)
	{
		this.minVer = minVer;
	}

	public void setMaxVer(int maxVer)
	{
		this.maxVer = maxVer;
	}

	public static void main(String[] args)
	{
		BDI_Login e = new BDI_Login();
		e.setUsr("980008271122334455");
		e.setPwd("1");
		e.setTs(new Date().getTime() / 1000);
		System.out.println(e);

		BDI_Util.dumpByteArray(e.encode());
	}
}
