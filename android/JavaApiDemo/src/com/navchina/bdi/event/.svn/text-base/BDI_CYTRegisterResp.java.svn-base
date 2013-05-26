package com.navchina.bdi.event;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.BDI_Util;

public class BDI_CYTRegisterResp extends BDI_Event
{
	private int errCode = 0xff;
	private long id = 0xffffffff;
	private byte[] account = new byte[16];
	private long createTime = 0;
	private int accountStatus = 0xff;

	public BDI_CYTRegisterResp()
	{
		super(BDI_Event.REDIRECT_RESP);
	}

	public BDI_CYTRegisterResp(byte[] buf)
	{
		super(buf);
		account = new byte[16];
		
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		BDI_Decoder dec = new BDI_Decoder(in);

		try {
			errCode = in.readByte();
			in.read(new byte[7]);// RESERVED
			id = dec.readUInt();
			in.read(account);
			createTime = dec.readUInt();
			accountStatus = in.readByte();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		String ret = "\t" + Integer.toHexString(errCode) + ", " + id + ", "
				+ new String(account) + ", " + BDI_Util.time2Str(createTime)
				+ ", " + Integer.toHexString(accountStatus) + "\n";
		return fh_String() + "\n" + ret;
	}

	public int getErrCode()
	{
		return errCode;
	}

	public long getId()
	{
		return id;
	}

	public byte[] getAccount()
	{
		return account;
	}

	public long getCreateTime()
	{
		return createTime;
	}

	public int getAccountStatus()
	{
		return accountStatus;
	}
}
