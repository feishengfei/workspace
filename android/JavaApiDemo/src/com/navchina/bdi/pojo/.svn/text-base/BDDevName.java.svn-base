package com.navchina.bdi.pojo;

import com.navchina.bdi.BDI_Util;

public class BDDevName
{
	private int addrType;
	private int addrStyle;
	private byte[] devNum;

	public int getAddrType()
	{
		return addrType;
	}

	public void setAddrType(int addrType)
	{
		this.addrType = addrType;
	}

	public int getAddrStyle()
	{
		return addrStyle;
	}

	public void setAddrStyle(int addrStyle)
	{
		this.addrStyle = addrStyle;
	}

	public byte[] getDevNum()
	{
		return devNum;
	}

	public void setDevNum(String devNum)
	{
		BDI_Util.copyStr2Bytes(devNum, this.devNum);
	}

	public BDDevName()
	{
		addrType = -1;
		addrStyle = -1;
		devNum = new byte[18];
	}
	
	@Override
	public String toString()
	{
		return "\t" + addrType +", " + addrStyle + ", " + new String(devNum) + "\n";
	}
	
	public static void main(String[] args)
	{
		BDDevName name = new BDDevName();
		name.setAddrStyle(0);
		name.setAddrType(0);
		name.setDevNum("255714");
		System.out.println(name);
	}
	
}
