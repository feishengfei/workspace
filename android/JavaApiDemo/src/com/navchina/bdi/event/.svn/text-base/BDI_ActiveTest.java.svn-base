package com.navchina.bdi.event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import com.navchina.bdi.BDI_Util;

public class BDI_ActiveTest extends BDI_Event
{
	private long cytMicroSeconds;
	private long cytSec;

	public BDI_ActiveTest()
	{
		super(BDI_Event.LINKHOLD);
		long now = new Date().getTime();
		cytSec = now / 1000;
		cytMicroSeconds = (now % 1000) * 1000;
	}

	@Override
	protected byte[] encodeBody() throws IOException
	{
		ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
		DataOutputStream d_out = new DataOutputStream(ba_out);
		d_out.writeInt((int) cytSec);
		d_out.writeInt((int) cytMicroSeconds);
		return ba_out.toByteArray();
	}

	@Override
	public String toString()
	{
		String res = "\t" + BDI_Util.time2Str(cytSec) + ", " + cytMicroSeconds + "\n";
		return fh_String() +"\n" + res;
	}

}
