package com.navchina.xzhou.basicType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.navchina.bdi.BDI_Decoder;
import com.navchina.bdi.pojo.BDDevName;

public class TestInputStream
{
	public static final String NAME = "abc.txt";
	
	public static void main(String[] args)
	{
		try {
			FileOutputStream fout = new FileOutputStream(NAME, true);
			DataOutputStream dout = new DataOutputStream(fout);
			dout.write("123456789000000000".getBytes());
			dout.close();
			
			FileInputStream fin = new FileInputStream(NAME);
			DataInputStream din = new DataInputStream(fin);
			BDI_Decoder dec = new BDI_Decoder(din);
			
			BDDevName dn = dec.getBDDevName();
			System.out.println(dn);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
