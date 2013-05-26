package com.navchina.xzhou.basicType;

import java.io.UnsupportedEncodingException;

import com.navchina.bdi.BDI_Util;

public class TestString
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//UTF-8 Unicode
		String hello = "你好123";
		System.out.println(hello);
		BDI_Util.dumpByteArray(hello.getBytes());
		
		//ansi(GBK)
		try {
			byte [ ] hello_ansi_bytes = {
				(byte) 0xc4, (byte) 0xe3, (byte) 0xba, (byte) 0xc3, 0x31, 0x32, 0x33
			};
			String hello_ansi = new String(hello_ansi_bytes, "GBK");
			System.out.println(hello_ansi);
			BDI_Util.dumpByteArray(hello_ansi.getBytes());
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}

}
