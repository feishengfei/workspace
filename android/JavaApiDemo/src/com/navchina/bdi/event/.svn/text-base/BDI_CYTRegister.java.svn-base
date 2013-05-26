package com.navchina.bdi.event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import com.navchina.bdi.BDI_Util;

public class BDI_CYTRegister extends BDI_Event
{
	private long usrId;
	private byte[] account;
	private byte[] pwd;
	private byte[] usrName;
	private int idType;
	private byte[] idNum;
	private byte[] mobileNum;
	private byte[] telNum;
	private byte[] homeAddr;
	private byte[] zipCode;
	private byte[] eMail;
	private long createTime;
	private int accountStatus;
	private int photoLen;
	private byte[] photo = null;

	public BDI_CYTRegister()
	{
		super(BDI_Event.REGISTER);
		usrId = 0xFFFFFFFF;
		account = new byte[16];
		pwd = new byte[20];
		usrName = new byte[20];
		idType = 0xff;
		idNum = new byte[20];
		mobileNum = new byte[20];
		telNum = new byte[20];
		homeAddr = new byte[40];
		zipCode = new byte[8];
		eMail = new byte[20];
		createTime = new Date().getTime() / 1000;
		accountStatus = 0x00;
		photoLen = 0;
		photo = null;
	}

	@Override
	protected byte[] encodeBody() throws IOException
	{
		ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(ba_out);

		out.writeInt((int) usrId);
		out.write(account);
		out.write(pwd);
		out.write(usrName);
		out.write(new byte[3]);// RESERVED
		out.writeByte(idType);
		out.write(idNum);
		out.write(mobileNum);
		out.write(telNum);
		out.write(homeAddr);
		out.write(zipCode);
		out.write(eMail);
		out.writeInt((int) createTime);
		out.writeByte(accountStatus);
		out.write(new byte[5]);// RESERVED
		out.writeShort(photoLen);
		// FIXME NO PHOTO

		return ba_out.toByteArray();
	}

	@Override
	public String toString()
	{
		String res = "\t" + new String(account) + "@" + new String(pwd) + ", "
				+ new String(usrName) + "\n" 
				+ "\t" + idType + ", " + new String(idNum) + "\n" 
				+ "\t" + new String(mobileNum)+ ", " + new String(telNum) + "\n" 
				+ "\t" + new String(homeAddr) + ", " + new String(zipCode) + ", " + new String(eMail) + "\n" 
				+ "\t" + BDI_Util.time2Str(createTime) + ", " + accountStatus + "\n";
		return fh_String() + "\n" + res;
	}

	public void setUsrId(long usrId)
	{
		this.usrId = usrId;
	}

	public void setAccount(String account)
	{
		BDI_Util.copyStr2Bytes(account, this.account);
	}

	public void setPwd(String pwd)
	{
		BDI_Util.copyStr2Bytes(pwd, this.pwd);
	}

	public void setUsrName(String usrName)
	{
		BDI_Util.copyStr2Bytes(usrName, this.usrName);
	}

	public void setIdType(int idType)
	{
		this.idType = idType;
	}

	public void setIdNum(String idNum)
	{
		BDI_Util.copyStr2Bytes(idNum, this.idNum);
	}

	public void setMobileNum(String mobileNum)
	{
		BDI_Util.copyStr2Bytes(mobileNum, this.mobileNum);
	}

	public void setTelNum(String telNum)
	{
		BDI_Util.copyStr2Bytes(telNum, this.telNum);
	}

	public void setHomeAddr(String homeAddr)
	{
		BDI_Util.copyStr2Bytes(homeAddr, this.homeAddr);
	}

	public void setZipCode(String zipCode)
	{
		BDI_Util.copyStr2Bytes(zipCode, this.zipCode);
	}

	public void seteMail(String eMail)
	{
		BDI_Util.copyStr2Bytes(eMail, this.eMail);
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public void setAccountStatus(int accountStatus)
	{
		this.accountStatus = accountStatus;
	}

	public void setPhotoLen(int photoLen)
	{
		this.photoLen = photoLen;
	}

	/*
	 * XXX public void setPhoto(byte[] photo) { this.photo = photo; }
	 */

}
