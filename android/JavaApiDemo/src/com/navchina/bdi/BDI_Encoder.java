package com.navchina.bdi;

import java.io.DataOutputStream;
import java.io.IOException;

import com.navchina.bdi.pojo.BDDevName;
import com.navchina.bdi.pojo.BDMessage;
import com.navchina.bdi.pojo.BDTermInfo;
import com.navchina.bdi.pojo.BDTermStatus;
import com.navchina.bdi.pojo.GPosition;

public class BDI_Encoder
{
	private final DataOutputStream out;

	public BDI_Encoder(DataOutputStream out)
	{
		this.out = out;
	}

	public DataOutputStream getOut()
	{
		return out;
	}

	public synchronized final void setGPosition(GPosition pos)
			throws IOException
	{
		if (null == pos)
			return;
		out.writeInt((int) pos.getTs());
		out.writeInt((int) (pos.getLon() * BDI.MEGA_I));
		out.writeInt((int) (pos.getLat() * BDI.MEGA_I));
		out.writeShort((int) (pos.getSpeed() / 0.036));
		out.writeShort(pos.getCourse());
		out.writeShort((int) (pos.getTurnRate() / 0.01));
		out.writeShort(pos.getAlt());
		out.writeByte(pos.getLocMode());
		out.writeByte(pos.getLocStatus());
		out.writeByte(pos.getCoordType());
		out.writeByte(pos.getpType());
	}

	public synchronized final void setBDDevName(BDDevName dn)
			throws IOException
	{
		if (null == dn)
			return;
		out.writeByte(dn.getAddrType());
		out.writeByte(dn.getAddrStyle());
		out.write(dn.getDevNum());
	}

	public synchronized final void setBDTermInfo(BDTermInfo ti) throws IOException
	{
		if (null == ti)
			return;
		out.writeByte(ti.getType());
		out.writeByte(ti.getStatus());
		out.writeByte(ti.isHasCarrier()? 0x01:0x00);
		out.write(new byte[5]);			//RESERVED
		out.write(ti.getId());
		out.write(ti.getAlias());
		out.write(new byte[3]);			//RESERVED
		out.writeByte(ti.getCarrierType());
		out.writeInt((int) ti.getCarrierId());
		out.write(ti.getCarrierName());
		out.write(ti.getCarrierAlias());
		out.write(new byte[4]);
		out.writeInt((int) ti.getCarrierOwnerOrg());
		out.write(ti.getCarrierOwnerName());
		out.writeShort(ti.getCarrierCapacity());
		out.write(new byte[22]);		//RESERVED
	}

	public synchronized final void setBDTermStatus(BDTermStatus ts)
			throws IOException
	{
		if (null == ts)
			return;
		out.writeByte(ts.getPower());
		out.writeByte(ts.getSat1());
		out.writeByte(ts.getSat2());
		out.writeByte(ts.getCellSignal());
		out.write(new byte[12]);		//RESERVED
	}

	public synchronized final void setBDMessage(BDMessage msg)
			throws IOException
	{
		out.writeByte(msg.getNchars());
		out.write(msg.getBuf());
	}

}
