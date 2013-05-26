package com.navchina.bdi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BDI_Util
{
	private static Logger log = null;
	private static AtomicLong curSerialNo = new AtomicLong(0);
	private static AtomicLong curPacketNo = new AtomicLong(0);

	public static final long getSerialNo()
	{
		return curSerialNo.getAndIncrement();
	}

	public static final Logger getLogger()
	{
		if (log == null) {
			log = Logger.getLogger("bdi");
			FileHandler fileHandler;
			try {
				fileHandler = new FileHandler("/tmp/bdi.log");
				fileHandler.setLevel(Level.ALL);
				fileHandler.setFormatter(new MyLogHander());
				log.addHandler(fileHandler);
			}
			catch (SecurityException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return log;
	}

	public static final long getPacketNo()
	{
		return curPacketNo.getAndIncrement();
	}

	public static final String time2Str(long ts)
	{
		String ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ret = sdf.format(new Date(ts * 1000));
		return ret;
	}

	public static final String nowStr()
	{
		String ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ret = sdf.format(new Date());
		return ret;
	}

	public static void dumpByteArray(byte[] buf)
	{
		Logger log = getLogger();
		String out = "";
		int i = 0;
		for (i = 0; i < buf.length; i++) {
			if (0 == i % 16)
				out += String.format("%04x:", i);
			if (0 == i % 8)
				out += " -";
			out += String.format(" %02x", buf[i]);
			if (15 == i % 16)
				out += "\n";
		}
		if ((i % 16) > 0)
			out += "\n";
		log.info(out);
	}

	public static void copyStr2Bytes(String str, byte[] buf)
	{
		if (str == null || buf == null) {
			throw new IllegalArgumentException("str or buf equals null!");
		}
		byte[] str_buf = str.getBytes();
		int len = str_buf.length < buf.length ? str_buf.length : buf.length;
		System.arraycopy(str_buf, 0, buf, 0, len);
	}

	public static void main(String[] args)
	{
		System.out.println(BDI_Util.time2Str(1335325774));
		BDI_Util.dumpByteArray("helloworld".getBytes());

		System.out.println(BDI_Util.nowStr());

		for (int i = 0; i < 10; i++) {
			if (i % 3 == 0) {
				System.out.println("packetNo:" + getPacketNo());
			}
			System.out.println("serialNo:" + getSerialNo());
		}
	}
}
