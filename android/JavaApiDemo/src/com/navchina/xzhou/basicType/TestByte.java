package com.navchina.xzhou.basicType;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.navchina.bdi.BDI_Util;
import com.navchina.xzhou.nio.Decoder;

public class TestByte
{

	/**
	 * @param args
	 */

	public static void main(String[] args)
	{
		/*
		 * byte is from -128 ~ 127
		 */

		byte[] d = new byte[] { (byte) 0xde, (byte) 0xad, (byte) 0xbe,
				(byte) 0xef, };

		Decoder dec = new Decoder(d);

		/*
		 * int i = dec.getInt(4); System.out.println("i = " + i);
		 * System.out.println(Integer.toHexString(i));
		 */

		long l = dec.getLong(4);
		System.out.println("l = " + l);
		System.out.println(Long.toHexString(l));

		byte[] test;
		try {
			test = "Test!".getBytes("UTF-16LE");
			for (byte b : test) {
				System.out.print(" " + (0xff & b));
			}
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println();
		
		String c = "你";
		byte[] cb = c.getBytes();
		System.out.println("" + cb.length + ":" + (0xff&cb[0]) + "," + cb[1] + "," + cb[2]);
		byte[] orig = "abcdefg".getBytes();
		byte[] tmp = Arrays.copyOfRange(orig, 2, 100);
		BDI_Util.dumpByteArray(tmp);
		
		int a = -1;
		switch (a) {
			case -1:
				System.out.println(a);
				break;
			default:
				System.out.println("a != -1");
				break;
		}
		
		long l2 = 1;
		System.out.println(l2);
		l2 = (l2 | 0x80000000);
		int i2 = (int) ((l2 << 32) >>> 32);
		System.out.println(l2 + ":" + Long.toHexString(l2));
		System.out.println(i2 + ":" + Integer.toHexString(i2));
		
		byte b255 = -1;
		int i255 = b255;
		System.out.println(Integer.toHexString(i255) + i255);
	}
	
	
}
