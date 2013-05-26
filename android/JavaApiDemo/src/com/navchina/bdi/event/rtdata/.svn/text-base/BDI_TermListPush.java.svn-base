package com.navchina.bdi.event.rtdata;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.navchina.bdi.BDI;
import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.BDI_Util;
import com.navchina.bdi.pojo.BDTermInfo;

public class BDI_TermListPush extends BDI_RTData
{
	private int errCode = -1;
	private int termCount = 0;
	private ArrayList<BDTermInfo> terms = null;

	public BDI_TermListPush(byte[] buf)
	{
		super(buf);

		byte[] dataBody = Arrays.copyOfRange(buf, BDI.FRAME_HEADER_LEN
				+ BDI.DATA_HEADER_LEN, buf.length);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(
				dataBody));
		BDI_Decoder dec = new BDI_Decoder(in);
		try {
			pid = dec.readUInt();
			in.read(new byte[10]);
			errCode = in.read();
			termCount = in.read();
			terms = new ArrayList<BDTermInfo>();
			for (int i = 0; i < termCount; i++) {
				BDTermInfo ti = dec.getBDTermInfo();
				terms.add(ti);
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		String res = "\t" + pid + ", " + errCode + ", " + termCount + "\n";
		Iterator<BDTermInfo> it = terms.iterator();
		while (it.hasNext()) {
			BDTermInfo bdTermInfo = (BDTermInfo) it.next();
			res += bdTermInfo.toString();
		}
		return fh_String() + dh_String() + "\n" + res;
	}

	public int getErrCode()
	{
		return errCode;
	}

	public int getTermCount()
	{
		return termCount;
	}

	public ArrayList<BDTermInfo> getTerms()
	{
		return terms;
	}

}
