package com.navchina.bdi.event.rtdata;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.navchina.bdi.BDI;
import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.pojo.BDDevName;

public class BDI_TermJoinPush extends BDI_RTData
{

	private BDDevName oa;
	private long groupNum;
	private boolean isAgree;

	public BDI_TermJoinPush(byte[] buf)
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
			groupNum = dec.readUInt();
			int agree = in.read();
			if (0x01 == agree) {
				isAgree = true;
			}
			else if (0x02 == agree) {
				isAgree = false;
			}
			in.read(new byte[3]);// RESERVED
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		String agreeStr = isAgree ? "agree" : "disagree";
		String res = "\t" + pid + ", " + groupNum + ", " + agreeStr + "\n" + oa;
		return fh_String() + dh_String() + "\n" + res;
	}
}
