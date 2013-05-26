package com.navchina.bdi.event.rtdata;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.navchina.bdi.BDI;
import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.pojo.BDDevName;
import com.navchina.bdi.pojo.BDTermStatus;
import com.navchina.bdi.pojo.GPosition;

public class BDI_TermPosReportPush extends BDI_RTData
{

	private BDDevName oa;
	private int termNum = 0;
	private BDTermStatus termStatus;
	private ArrayList<GPosition> posList;

	public BDI_TermPosReportPush(byte[] buf)
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
			in.read(new byte[7]);
			termNum = in.read();
			termStatus = dec.getBDTermStatus();
			posList = new ArrayList<GPosition>();
			for (int i = 0; i < termNum; i++) {
				GPosition pos = dec.getPosition();
				posList.add(pos);
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString()
	{
		String res = "\t" + pid + "\n" + oa + termStatus;
		Iterator<GPosition> it = posList.iterator();
		while (it.hasNext()) {
			GPosition pos = it.next();
			res += pos.toString();
		}
		return fh_String() + dh_String() + "\n" + res;
	}

	public synchronized BDDevName getOa()
	{
		return oa;
	}

	public synchronized int getTermNum()
	{
		return termNum;
	}

	public synchronized BDTermStatus getTermStatus()
	{
		return termStatus;
	}

	public synchronized ArrayList<GPosition> getPosList()
	{
		return posList;
	}

}
