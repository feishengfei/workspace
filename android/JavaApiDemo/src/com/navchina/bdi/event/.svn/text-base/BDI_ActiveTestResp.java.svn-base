package com.navchina.bdi.event;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.BDI_Util;

public class BDI_ActiveTestResp extends BDI_Event
{
	private long cytSec;
	private long cytMicroSeconds;
	private long fosSec;
	private long fosMicroSeconds;

	public BDI_ActiveTestResp(byte[] buf)
	{
		super(buf);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		BDI_Decoder dec = new BDI_Decoder(in);

		try {
			cytSec = dec.readUInt();
			cytMicroSeconds = dec.readUInt();
			fosSec = dec.readUInt();
			fosMicroSeconds = dec.readUInt();

			// FIXME impl sync time here!
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString()
	{
		String res = "\t" + BDI_Util.time2Str(cytSec) + ", " + cytMicroSeconds
				+ ", " + BDI_Util.time2Str(fosSec) + ", " + fosMicroSeconds + "\n";
		return fh_String() + "\n" + res;
	}

	public long getCytSec()
	{
		return cytSec;
	}

	public long getCytMicroSeconds()
	{
		return cytMicroSeconds;
	}

	public long getFosSec()
	{
		return fosSec;
	}

	public long getFosMicroSeconds()
	{
		return fosMicroSeconds;
	}

}
