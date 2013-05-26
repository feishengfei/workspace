package com.navchina.bdi.event;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.navchina.bdi.BDI_Decoder;

public class BDI_REdirectInd extends BDI_Event
{

	private byte[] rHost0;
	private byte[] rHost1;
	private byte[] rHost2;
	
	private int rFamily0;
	private long rPort0;
	private int rFamily1;
	private long rPort1;
	private int rFamily2;
	private long rPort2;

	public BDI_REdirectInd(byte[] buf)
	{
		super(buf);
		rHost0 = new byte[40];
		rHost1 = new byte[40];
		rHost2 = new byte[40];

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		BDI_Decoder dec = new BDI_Decoder(in);

		try {
			rFamily0 = in.read();
			in.read(new byte[3]);
			rPort0 = dec.readUInt();
			in.read(rHost0);
			
			rFamily1 = in.read();
			in.read(new byte[3]);
			rPort1 = dec.readUInt();
			in.read(rHost1);
			
			rFamily2 = in.read();
			in.read(new byte[3]);
			rPort2 = dec.readUInt();
			in.read(rHost2);
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString()
	{
		String res = "\t" + rFamily0 + ", " + rPort0 + ", " + new String(rHost0) + "\n"
		+ "\t" + rFamily1 + ", " + rPort1 + ", " + new String(rHost1) + "\n"
		+ "\t" + rFamily2 + ", " + rPort2 + ", " + new String(rHost2);

		return fh_String() + "\n" + res;
	}
}
