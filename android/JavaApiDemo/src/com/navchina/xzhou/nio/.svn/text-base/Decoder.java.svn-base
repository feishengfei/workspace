package com.navchina.xzhou.nio;

public class Decoder
{
	private int mark = 0;
	private byte[] buf = null;

	public Decoder(byte[] buf)
	{
		this.mark = 0;
		this.buf = buf;
	}

	public int getInt(int len)
	{
		if (mark + len > buf.length) {
			return 0;
		}

		int ret = 0;
		for (int j = 0; j < len; j++) {
			ret += ((buf[mark] & 0xff) << 8 * (len - 1 - j));
			mark++;
		}
		return ret;
	}

	public long getLong(int len)
	{
		long ret = 0;
		ret = getInt(len);
		return ret < 0 ? (ret << 32) >>> 32 : ret;
	}

	public String getStr(int size)
	{
		String ret;
		int byteCount = (mark + size) < buf.length ? size : buf.length - mark;
		ret = new String(buf, mark, byteCount);
		mark += byteCount;
		return ret;
	}

}