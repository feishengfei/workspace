package com.navchina.bdi.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import com.navchina.bdi.BDI;
import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.event.rtdata.BDI_RTData;

/**
 * @author xzhou
 * 
 */
public class BDI_Event
{
	public static final int REGISTER = 0x02010000;
	public static final int REGISTER_RESP = 0x82010000;
	public static final int LOGIN = 0x02010001;
	public static final int LOGIN_RESP = 0x82010001;
	public static final int LINKHOLD = 0x02010002;
	public static final int LINKHOLD_RESP = 0x82010002;
	public static final int REDIRECT_IND = 0x02010003;
	public static final int REDIRECT_RESP = 0x82010003;
	public static final int LOGOUT = 0x02010010;
	public static final int LOGOUT_RESP = 0x82010010;
	public static final int RTDATA = 0x02010100;
	public static final int RTDATA_RESP = 0x82010100;
	public static final int QUREY = 0x02010200;
	public static final int REPLY = 0x82010200;
	public static final int USER = 0xFF000000;

	// header part 16 bytes
	private long type; // 帧类型
	private long sn; // 帧流水号
	private int ver; // 协议版本号
	private int compEncryp;// 压缩和加密方式
	private int status; // 状态

	// rt_data common 8 bytes
	protected long bid; // 业务类型
	protected int priority; // 优先级

	// rt_data content
	protected long pid; // package id
	protected byte[] data; // 帧体
	protected long dataSize; // 帧体长度

	/**
	 * @param type
	 *            帧类型
	 */
	public BDI_Event(long type)
	{
		super();

		this.type = type;
		sn = 0;
		ver = 0x10;
		compEncryp = 0;
		status = 0;

		bid = 0;
		priority = BDI.PRIORITY_NOMAL;

		pid = 0;
		dataSize = 0;
		data = null;
	}

	/**
	 * @param buf
	 *            整个帧
	 */
	public BDI_Event(byte[] buf)
	{
		super();

		sn = 0;
		ver = 0x10;
		compEncryp = 0;
		status = 0;

		bid = 0;
		priority = BDI.PRIORITY_NOMAL;

		pid = 0;
		dataSize = 0;
		data = null;

		if (null == buf || buf.length < BDI.FRAME_HEADER_LEN)
			throw new IllegalArgumentException("BDI_Event constructer error");

		ByteArrayInputStream ba_in = new ByteArrayInputStream(buf);
		DataInputStream in = new DataInputStream(ba_in);
		BDI_Decoder dec = new BDI_Decoder(in);
		try {
			dataSize = dec.readUInt() - BDI.FRAME_HEADER_LEN;

			type = in.readInt();
			sn = dec.readUInt();
			ver = in.read();
			compEncryp = in.read();
			status = in.read();
			in.read(); // RESERVED
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		bid = 0;
		pid = 0;
		priority = BDI.PRIORITY_NOMAL;

		data = Arrays.copyOfRange(buf, BDI.FRAME_HEADER_LEN, buf.length);
	}

	/**
	 * @param buf
	 *            整个帧header + body
	 * @return null参数错误 / 解析的事件
	 */
	public static BDI_Event decode(byte[] buf)
	{
		if (null == buf || buf.length < BDI.FRAME_HEADER_LEN)
			return null;

		BDI_Event ret = null;

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(buf));
		try {
			in.readInt();// 跳过类型的4个字节
			switch (in.readInt()) {
				case LINKHOLD_RESP:
					ret = new BDI_ActiveTestResp(buf);
					break;
				case REGISTER_RESP:
					ret = new BDI_CYTRegisterResp(buf);
					break;
				case LOGIN_RESP:
					ret = new BDI_LoginResp(buf);
					break;
				case REDIRECT_RESP:
					ret = new BDI_REdirectInd(buf);
					break;
				case RTDATA:
					ret = BDI_RTData.decode(buf);
					break;
				case REPLY: // TODO
				default:
					ret = new BDI_Event(buf);
					break;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private void encodeHeader(OutputStream out)
	{
		DataOutputStream dout = new DataOutputStream(out);
		try {
			dout.writeInt((int) (dataSize + BDI.FRAME_HEADER_LEN));
			dout.writeInt((int) type);
			dout.writeInt((int) sn);
			dout.writeByte(ver);
			dout.writeByte(compEncryp);
			dout.writeByte(status);
			dout.writeByte(0);
		}
		catch (IOException e) {
			System.out.println(Long.toHexString(type) + ":"
					+ Long.toHexString(bid) + " encode Header Error");
		}
	}

	/**
	 * @return
	 */
	protected byte[] encodeBody() throws IOException
	{
		if (data != null) {
			return data;
		}
		return new byte[(int) dataSize];
	}

	/**
	 * @return 返回事件编码后的数组
	 */
	public byte[] encode()
	{
		byte[] body = null;
		try {
			body = encodeBody();
		}
		catch (IOException e2) {
			System.out.println(Long.toHexString(type) + ":"
					+ Long.toHexString(bid) + " gen Body Error");
			return null;
		}
		dataSize = body.length;
		ByteArrayOutputStream ba_out = new ByteArrayOutputStream(
				(int) (dataSize + BDI.FRAME_HEADER_LEN));
		DataOutputStream dout = new DataOutputStream(ba_out);
		encodeHeader(dout);
		try {
			dout.write(body);
		}
		catch (IOException e1) {
			System.out.println(Long.toHexString(type) + ":"
					+ Long.toHexString(bid) + " write Body Error");
		}
		return ba_out.toByteArray();
	}

	protected String fh_String()
	{
		String type_str = null;
		switch ((int) type) {
			case REGISTER:
				type_str = "Register";
				break;
			case REGISTER_RESP:
				type_str = "RegisterResp";
				break;
			case LOGIN:
				type_str = "Login";
				break;
			case LOGIN_RESP:
				type_str = "LoginResp";
				break;
			case LOGOUT:
				type_str = "Logout";
				break;
			case LOGOUT_RESP:
				type_str = "LogoutResp";
				break;
			case REDIRECT_IND:
				type_str = "RedirectInd";
				break;
			case RTDATA:
				type_str = "RTData";
				break;
			case RTDATA_RESP:
				type_str = "RTDataResp";
				break;
			case QUREY:
				type_str = "Query";
				break;
			case REPLY:
				type_str = "Reply";
				break;
			case LINKHOLD:
				type_str = "LinkHold";
				break;
			case LINKHOLD_RESP:
				type_str = "LinkHoldResp";
				break;
			default:
				type_str = "Type_" + Integer.toHexString((int) type);
		}
		return "[" + type_str + ", " + sn + ", " + Integer.toHexString(ver)
				+ ", " + Integer.toHexString(compEncryp) + ", " + status + "]";
	}

	/**
	 * @return 数据帧帧头
	 */
	public String dh_String()
	{
		String bidStr = "";
		switch((int)bid) {
			case BDI.TermListEvent:
				bidStr = "TermListEvent";
				break;
			case BDI.InviteTermJoinGroup:
				bidStr = "InviteTermJoinGroup";
				break;
			case BDI.InviteTermJoinGroupReply:
				bidStr = "InviteTermJoinGroupReply";
				break;
			case BDI.GetPosReport:
				bidStr = "GetPosReport";
				break;
			case BDI.TermPosReply:
				bidStr = "TermPosReply";
				break;
			case BDI.TermPosReport:
				bidStr = "TermPosReport";
				break;
			case BDI.TermSosEvent:
				bidStr = "TermSosEvent";
				break;
			case BDI.SubmitMessageEvent:
				bidStr = "SubmitMessageEvent";
				break;
			case BDI.DeliverMessageEvent:
				bidStr = "DeliverMessageEvent";
				break;
			case BDI.ReceiptMessageEvent:
				bidStr = "ReceiptMessageEvent";
				break;
			default:
				bidStr = "bid=" + Long.toHexString(bid);
				break;
		}
		return "[" + bidStr + ", " + priority + "]";
	}

	@Override
	public String toString()
	{
		String ret = fh_String();
		if (0 == dataSize) {
			ret += "...\n";
		}
		return ret;
	}

	public long getType()
	{
		return type;
	}

	public void setType(long type)
	{
		this.type = type;
	}

	public long getSn()
	{
		return sn;
	}

	public void setSn(long sn)
	{
		this.sn = sn;
	}

	public int getVer()
	{
		return ver;
	}

	public void setVer(int ver)
	{
		this.ver = ver;
	}

	public int getCompEncryp()
	{
		return compEncryp;
	}

	public void setCompEncryp(int compEncryp)
	{
		this.compEncryp = compEncryp;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public long getBid()
	{
		return bid;
	}

	public void setBid(long bid)
	{
		this.bid = bid;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public long getPid()
	{
		return pid;
	}

	public void setPid(long pid)
	{
		this.pid = pid;
	}
}
