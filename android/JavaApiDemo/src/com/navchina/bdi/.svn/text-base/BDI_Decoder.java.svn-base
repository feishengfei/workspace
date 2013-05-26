package com.navchina.bdi;

import java.io.DataInputStream;
import java.io.IOException;

import com.navchina.bdi.pojo.BDDevName;
import com.navchina.bdi.pojo.BDMessage;
import com.navchina.bdi.pojo.BDTermInfo;
import com.navchina.bdi.pojo.BDTermStatus;
import com.navchina.bdi.pojo.GPosition;

public class BDI_Decoder
{
	private final DataInputStream in;

	public BDI_Decoder(DataInputStream in)
	{
		this.in = in;
	}

	public DataInputStream getIn()
	{
		return in;
	}

	/**
	 * @return 从流中按4个字节读取 uint32，以long的形式存储
	 * @throws IOException
	 */
	public final long readUInt() throws IOException
	{
		long ret = -1;
		ret = in.readInt();
		if (ret < 0) {
			ret = Math.abs(ret);
		}
		return ret;
	}

	/**
	 * @return 从流中按2个字节读取 uint16，以int的形式存储
	 * @throws IOException
	 */
	public final int readUShort() throws IOException
	{
		int ret = in.readShort();
		if (ret < 0) {
			ret = Math.abs(ret);
		}
		return ret;
	}

	public final GPosition getPosition() throws IOException
	{
		GPosition pos = new GPosition();
		pos.setTs(readUInt());
		pos.setLon(in.readInt() / BDI.MEGA_D);
		pos.setLat(in.readInt() / BDI.MEGA_D);
		pos.setSpeed(in.readShort() * 0.036);
		pos.setCourse(in.readShort());
		pos.setTurnRate(in.readShort() * 0.01);
		pos.setAlt(in.readShort());
		pos.setLocMode(in.read());
		pos.setLocStatus(in.read());
		pos.setCoordType(in.read());
		pos.setpType(in.read());
		return pos;
	}

	public final BDDevName getBDDevName() throws IOException
	{
		BDDevName dn = new BDDevName();
		dn.setAddrType(in.read());
		dn.setAddrStyle(in.read());
		in.read(dn.getDevNum());
		return dn;
	}

	public final BDTermInfo getBDTermInfo() throws IOException
	{
		BDTermInfo ti = new BDTermInfo();
		ti.setHasCarrier(false);

		ti.setType(in.read());
		ti.setStatus(in.read());
		int hasCarrier = in.read();
		if (0x00 == hasCarrier) {
			ti.setHasCarrier(false);
		}
		else if (0x01 == hasCarrier) {
			ti.setHasCarrier(true);
		}
		in.read(new byte[5]); // RESERVED
		in.read(ti.getId());
		in.read(ti.getAlias());
		in.read(new byte[3]); // RESERVED
		ti.setCarrierType(in.read());
		ti.setCarrierId(readUInt());
		in.read(ti.getCarrierName());
		in.read(ti.getCarrierAlias());
		in.read(new byte[4]); // RESERVED
		ti.setCarrierOwnerOrg(readUInt());
		in.read(ti.getCarrierOwnerName());
		ti.setCarrierCapacity(readUShort());
		in.read(new byte[22]); // RESERVED
		return ti;
	}

	public final BDTermStatus getBDTermStatus() throws IOException
	{
		BDTermStatus ts = new BDTermStatus();
		ts.setPower(in.read());
		ts.setSat1(in.read());
		ts.setSat2(in.read());
		in.read(new byte[5]);
		return ts;
	}

	public final BDMessage getBDMessage() throws IOException
	{
		BDMessage msg = new BDMessage();
		msg.setNchars(in.read());
		msg.setBuf(new byte[msg.getNchars()]);
		in.read(msg.getBuf());
		return msg;
	}
}
