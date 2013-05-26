package com.navchina.bdi.event.rtdata;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import com.navchina.bdi.BDI;
import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.BDI_Util;
import com.navchina.bdi.pojo.BDDevName;
import com.navchina.bdi.pojo.BDMessage;
import com.navchina.bdi.pojo.BDTermStatus;
import com.navchina.bdi.pojo.GPosition;

public class BDI_TermSosPush extends BDI_RTData
{
	public static final int SOS_UNKNOWNREASON = 0x00;
	public static final int SOS_CRASH = 0x01;
	public static final int SOS_STORM = 0x02;
	public static final int SOS_FIRE = 0x03;
	public static final int SOS_GROUNDED = 0x04;
	public static final int SOS_SICK = 0x05;
	public static final int SOS_DETAINED = 0x06;
	public static final int SOS_ENGINETROUBLE = 0x07;
	public static final int SOS_ENEMY = 0x30;
	public static final int SOS_CANCEL = 0xA0;

	private BDDevName oa;
	private long ts;
	private int sosType;
	private BDTermStatus termStatus;
	private GPosition pos;
	private byte[] site = new byte[20];
	private BDMessage msg;

	public BDI_TermSosPush(byte[] buf)
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
			ts = dec.readUInt();
			in.read(new byte[3]);// RESERVED
			sosType = in.readByte();
			termStatus = dec.getBDTermStatus();
			pos = dec.getPosition();
			in.read(site);
			in.read(new byte[3]);// RESERVED
			msg = dec.getBDMessage();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		String sosStr = "";
		switch (sosType) {
			case SOS_CANCEL:
				sosStr = "SOS Cancel";
				break;
			case SOS_CRASH:
				sosStr = "Ship Crash";
				break;
			case SOS_DETAINED:
				sosStr = "Ship Detained";
				break;
			case SOS_ENEMY:
				sosStr = "Encounter Enemy";
				break;
			case SOS_ENGINETROUBLE:
				sosStr = "Ship Engine Failure";
				break;
			case SOS_FIRE:
				sosStr = "Fire";
				break;
			case SOS_GROUNDED:
				sosStr = "Ship Grounded";
				break;
			case SOS_SICK:
				sosStr = "Sailor Sick";
				break;
			case SOS_STORM:
				sosStr = "Encounter Storm";
				break;
			case SOS_UNKNOWNREASON:
			default:
				sosStr = "Unkown Reason SOS";
				break;
		}
		String res = "\t" + pid + ", " + BDI_Util.time2Str(ts) + ", " + sosStr
				+ "\n" + oa + termStatus + pos + msg;
		return fh_String() + dh_String() + "\n" + res;
	}

	public synchronized BDDevName getOa()
	{
		return oa;
	}

	public synchronized long getTs()
	{
		return ts;
	}

	public synchronized int getSosType()
	{
		return sosType;
	}

	public synchronized BDTermStatus getTermStatus()
	{
		return termStatus;
	}

	public synchronized GPosition getPos()
	{
		return pos;
	}

	public synchronized byte[] getSite()
	{
		return site;
	}

	public synchronized BDMessage getMsg()
	{
		return msg;
	}

}
