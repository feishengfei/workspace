package com.navchina.bdi.event;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.BDI_Util;

public class BDI_LoginResp extends BDI_Event
{
	private static final int RESP_SUCCESS = 0x00;
	private static final int RESP_REDIRECT = 0x01;
	private static final int RESP_SERV_BUSY = 0x20;
	private static final int RESP_SERV_OVERLOAD = 0x21;
	private static final int RESP_UNSUPPORT_VER = 0x22;
	private static final int RESP_WRONG_USR = 0x23;
	private static final int RESP_WRONG_PWD = 0x24;
	private static final int RESP_WRONG_TIME = 0x25;
	private static final int RESP_UNSUPPORT_LM = 0x26;
	private static final int RESP_RELOGIN = 0x27;

	private long fosSec;
	private long fosMicroSecond;
	private int errCode;
	private int maxVer;
	private int minVer;
	private int rFamily0;
	private long rPort0;
	private byte[] rHost0 = new byte[40];
	private int rFamily1;
	private long rPort1;
	private byte[] rHost1 = new byte[40];
	private int rFamily2;
	private long rPort2;
	private byte[] rHost2 = new byte[40];
	private byte[] authServer = new byte[20];
	private int channel;
	
	public BDI_LoginResp(int errCode) {
		super(BDI_Event.LOGIN_RESP);
		fosSec = 0;
		fosMicroSecond = 0;
		this.errCode = errCode;
		minVer = 0;
		maxVer = 0;
		channel = -1;
	}

	public BDI_LoginResp(byte[] buf)
	{
		super(buf);

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		BDI_Decoder dec = new BDI_Decoder(in);

		try {
			fosSec = dec.readUInt();
			fosMicroSecond = dec.readUInt();
			errCode = in.read();
			maxVer = in.read();
			minVer = in.read();
			in.read(); // RESERVED

			rFamily0 = in.read();
			in.read(new byte[3]);// RESERVED
			rPort0 = dec.readUInt();
			in.read(rHost0);

			rFamily1 = in.read();
			in.read(new byte[3]);// RESERVED
			rPort1 = dec.readUInt();
			in.read(rHost1);

			rFamily2 = in.read();
			in.read(new byte[3]);// RESERVED
			rPort2 = dec.readUInt();
			in.read(rHost2);

			in.read(authServer);

			in.read(new byte[8]);// RESERVED

			channel = -1;

			// FIXME sync with Server

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString()
	{
		String errStr = "";
		switch(errCode) {
			case RESP_REDIRECT:
				errStr = "Redirect";
				break;
			case RESP_RELOGIN:
				errStr = "Relogin Not Allowed";
				break;
			case RESP_SERV_BUSY:
				errStr = "Server Busy";
				break;
			case RESP_SERV_OVERLOAD:
				errStr = "Server Overloaded";
				break;
			case RESP_SUCCESS:
				errStr = "Login Success";
				break;
			case RESP_UNSUPPORT_LM:
				errStr = "Unsupport Login Mode";
				break;
			case RESP_UNSUPPORT_VER:
				errStr = "Unsupport Client Ver";
				break;
			case RESP_WRONG_PWD:
				errStr = "Password Wrong";
				break;
			case RESP_WRONG_TIME:
				errStr = "Time Need Sync";
				break;
			case RESP_WRONG_USR:
				errStr = "Account Wrong";
				break;
			default:
				errStr = "errCode" + Integer.toHexString(errCode);
				break;
		}
		
		errStr = "\"" +  errStr + "\"";
	
		String res = "\t" + errStr + ", " + BDI_Util.time2Str(fosSec) + ", " + fosMicroSecond
				+ ", " + maxVer + ", " + minVer + "\n"
				+ "\t" + rFamily0 + ", " + rPort0 + ", " + new String(rHost0) + "\n"
				+ "\t" + rFamily1 + ", " + rPort1 + ", " + new String(rHost1) + "\n"
				+ "\t" + rFamily2 + ", " + rPort2 + ", " + new String(rHost2) + "\n"
				+ "\t" + new String(authServer) + "\n";
		return fh_String() + "\n" + res;
	}

	public int getChannel()
	{
		return channel;
	}

	public void setChannel(int channel)
	{
		this.channel = channel;
	}

	public long getFosSec()
	{
		return fosSec;
	}

	public long getFosMicroSecond()
	{
		return fosMicroSecond;
	}

	public int getErrCode()
	{
		return errCode;
	}

	public int getMaxVer()
	{
		return maxVer;
	}

	public int getMinVer()
	{
		return minVer;
	}

	public int getrFamily0()
	{
		return rFamily0;
	}

	public long getrPort0()
	{
		return rPort0;
	}

	public byte[] getrHost0()
	{
		return rHost0;
	}

	public int getrFamily1()
	{
		return rFamily1;
	}

	public long getrPort1()
	{
		return rPort1;
	}

	public byte[] getrHost1()
	{
		return rHost1;
	}

	public int getrFamily2()
	{
		return rFamily2;
	}

	public long getrPort2()
	{
		return rPort2;
	}

	public byte[] getrHost2()
	{
		return rHost2;
	}

	public byte[] getAuthServer()
	{
		return authServer;
	}
	
}
