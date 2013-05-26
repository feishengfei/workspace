package com.navchina.xzhou.sendrecvdata;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BruteForceCoding
{
	private static byte byteVal = 101;
	private static short shortVal = 10001;
	private static int intVal = 100000001;
	private static long longVal = 1000000000001L;

	private final static int BSIZE = Byte.SIZE;
	private final static int SSIZE = Short.SIZE;
	private final static int ISIZE = Integer.SIZE;
	private final static int LSIZE = Long.SIZE;

	private final static int BYTEMASK = 0xff;

	/**
	 * @param buf
	 *            待转换的byte数组
	 * @return 十进制表示的字符串
	 */
	public static String byteArrayToDecimalString(byte[] buf)
	{
		StringBuilder rtn = new StringBuilder();
		for (byte b : buf) {
			rtn.append(Integer.toHexString(b&BYTEMASK)).append(" ");
		}
		return rtn.toString();
	}

	/**
	 * @param dst 编码的目的byte数组
	 * @param val 待转换的值
	 * @param offset 转换前的偏移量
	 * @param size 占几个字节
	 * @return 转换后的偏移量
	 */
	public static int encodeIntBigEndian(byte[] dst, long val, int offset,
			int size)
	{
		for (int i = 0; i < size; i++) {
			dst[offset++] = (byte) (val >> ((size - i - 1) * Byte.SIZE));
		}
		return offset;
	}

	/**
	 * @param val
	 * @param offset
	 * @param size
	 * @return 解码值
	 */
	public static long decodeIntBigEndian(byte[] val, int offset, int size)
	{
		long rtn = 0;
		for (int i = 0; i < size; i++) {
			rtn = (rtn << Byte.SIZE) | ((long) val[offset + i] & BYTEMASK);
		}
		return rtn;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
//		System.out.println(BSIZE + ", " + SSIZE + ", " + ISIZE + ", " + LSIZE);
		byte[] message = new byte[BSIZE + SSIZE + ISIZE + LSIZE];
		
		//encode
		int offset = encodeIntBigEndian(message, byteVal, 0, BSIZE);
		offset = encodeIntBigEndian(message, shortVal, offset, SSIZE);
		offset = encodeIntBigEndian(message, intVal, offset, ISIZE);
		offset = encodeIntBigEndian(message, longVal, offset, LSIZE);
		System.out.println("编码信息:" + byteArrayToDecimalString(message));
		
		//decode
		long value = decodeIntBigEndian(message, BSIZE, SSIZE);
		System.out.println("short = " + value);
		value = decodeIntBigEndian(message, BSIZE+SSIZE+ISIZE, LSIZE);
		System.out.println("long = " + value);
		
		//another way
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);
		try {
			out.writeByte(byteVal);
			out.writeShort(shortVal);
			out.writeInt(intVal);
			out.writeLong(longVal);
			out.flush();
			
			System.out.println("msg:" + byteArrayToDecimalString(buf.toByteArray()) );
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error on write:" + e.getMessage());
		}
	}

}
