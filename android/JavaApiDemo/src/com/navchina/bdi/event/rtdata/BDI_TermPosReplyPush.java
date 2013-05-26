package com.navchina.bdi.event.rtdata;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.navchina.bdi.BDI;
import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.pojo.BDDevName;
import com.navchina.bdi.pojo.BDTermStatus;
import com.navchina.bdi.pojo.GPosition;

public class BDI_TermPosReplyPush extends BDI_RTData
{

	private BDDevName oa;
	private GPosition pos;
	private BDTermStatus termStatus;

	public BDI_TermPosReplyPush(byte[] buf)
	{
		super(buf);

		byte[] dataBody = Arrays.copyOfRange(buf, BDI.FRAME_HEADER_LEN
				+ BDI.DATA_HEADER_LEN, buf.length);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(
				dataBody));
		BDI_Decoder dec = new BDI_Decoder(in);

		try {
			pid = dec.readUInt();
			oa = dec.getBDDevName();
			in.read(new byte[8]);// RESERVED
			termStatus = dec.getBDTermStatus();
			pos = dec.getPosition();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString()
	{
		String res = "\t" + pid + "\n" 
				+ oa.toString() + termStatus.toString()
				+ pos.toString();
		return fh_String() + dh_String() + "\n" + res;
	}

	public synchronized BDDevName getOa()
	{
		return oa;
	}

	public synchronized GPosition getPos()
	{
		return pos;
	}

	public synchronized BDTermStatus getTermStatus()
	{
		return termStatus;
	}
	
}
