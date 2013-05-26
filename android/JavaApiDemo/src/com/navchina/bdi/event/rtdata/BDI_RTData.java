package com.navchina.bdi.event.rtdata;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.navchina.bdi.BDI;
import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.BDI_Util;
import com.navchina.bdi.event.BDI_Event;
import com.navchina.bdi.event.rtdata.message.BDI_MessageEvent;

/**
 * @author xzhou
 * 
 */
public class BDI_RTData extends BDI_Event
{
	/**
	 * @param datatype
	 *            业务类型
	 */
	public BDI_RTData(long datatype)
	{
		super(BDI_Event.RTDATA);
		bid = datatype;
	}

	/**
	 * @param buf
	 *            整个帧 head + body
	 */
	public BDI_RTData(byte[] buf)
	{
		super(buf);
		byte[] body = Arrays.copyOfRange(buf, BDI.FRAME_HEADER_LEN, buf.length);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(body));
		BDI_Decoder dec = new BDI_Decoder(in);
		try {
			bid = in.readInt();
			priority = in.read();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param buf
	 *            整个帧 head + body
	 * @return null 参数错误 / 事件
	 */
	public static BDI_Event decode(byte[] buf)
	{
		BDI_Event res = null;
		if (null == buf
				|| buf.length <= BDI.FRAME_HEADER_LEN + BDI.DATA_HEADER_LEN)
			return null;

		byte[] body = Arrays.copyOfRange(buf, BDI.FRAME_HEADER_LEN, buf.length);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(body));
		int ibid = 0;
		try {
			ibid = in.readInt();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		switch (ibid) {
			case BDI.TermListEvent:
				res = new BDI_TermListPush(buf);
				break;
			case BDI.InviteTermJoinGroupReply:
				res = new BDI_TermJoinPush(buf);
				break;
			case BDI.TermPosReply:
				res = new BDI_TermPosReplyPush(buf);
				break;
			case BDI.TermPosReport:
				res = new BDI_TermPosReportPush(buf);
				break;
			case BDI.TermSosEvent:
				res = new BDI_TermSosPush(buf);
				break;
			case BDI.ReceiptMessageEvent:
			case BDI.DeliverMessageEvent:
			case BDI.SubmitMessageEvent:
				res = BDI_MessageEvent.decode(buf);
				break;
			default:
				res = new BDI_RTData(buf);
				break;
		}

		return res;
	}

	/**
	 * @return 针对接收自服务器事件来构造的应答事件
	 */
	public BDI_Event ack()
	{
		BDI_Event res = new BDI_Event(BDI_Event.RTDATA_RESP);
		if (null != res) {
			res.setSn(getSn());
			res.setBid(getBid() | 0x80000000); // FIXME
			res.setPid(getPid());
			res.setPriority(getPriority());
		}
		return res;
	}
}
