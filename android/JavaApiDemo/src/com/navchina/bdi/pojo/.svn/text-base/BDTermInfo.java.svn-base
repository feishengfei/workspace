package com.navchina.bdi.pojo;

import com.navchina.bdi.BDI_Util;

public class BDTermInfo
{
	private int type;
	private int status;
	private boolean hasCarrier;
	private byte[] id;
	private byte[] alias;
	private int carrierType;
	private long carrierId;
	private byte[] carrierName;
	private byte[] carrierAlias;
	private long carrierOwnerOrg;
	private byte[] carrierOwnerName;
	private int carrierCapacity;

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public boolean isHasCarrier()
	{
		return hasCarrier;
	}

	public void setHasCarrier(boolean hasCarrier)
	{
		this.hasCarrier = hasCarrier;
	}

	public byte[] getId()
	{
		return id;
	}

	public void setId(String id)
	{
		BDI_Util.copyStr2Bytes(id, this.id);
	}

	public byte[] getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		BDI_Util.copyStr2Bytes(alias, this.alias);
	}

	public int getCarrierType()
	{
		return carrierType;
	}

	public void setCarrierType(int carrierType)
	{
		this.carrierType = carrierType;
	}

	public long getCarrierId()
	{
		return carrierId;
	}

	public void setCarrierId(long carrierId)
	{
		this.carrierId = carrierId;
	}

	public byte[] getCarrierName()
	{
		return carrierName;
	}

	public void setCarrierName(String carrierName)
	{
		BDI_Util.copyStr2Bytes(carrierName, this.carrierName);
	}

	public byte[] getCarrierAlias()
	{
		return carrierAlias;
	}

	public void setCarrierAlias(String carrierAlias)
	{
		BDI_Util.copyStr2Bytes(carrierAlias, this.carrierAlias);
	}

	public long getCarrierOwnerOrg()
	{
		return carrierOwnerOrg;
	}

	public void setCarrierOwnerOrg(long carrierOwnerOrg)
	{
		this.carrierOwnerOrg = carrierOwnerOrg;
	}

	public byte[] getCarrierOwnerName()
	{
		return carrierOwnerName;
	}

	public void setCarrierOwnerName(String carrierOwnerName)
	{
		BDI_Util.copyStr2Bytes(carrierOwnerName, this.carrierOwnerName);
	}

	public int getCarrierCapacity()
	{
		return carrierCapacity;
	}

	public void setCarrierCapacity(int carrierCapacity)
	{
		this.carrierCapacity = carrierCapacity;
	}

	public BDTermInfo()
	{
		type = -1;
		status = -1;
		hasCarrier = false;
		id = new byte[16];
		alias = new byte[24];
		carrierType = -1;
		carrierId = 0;
		carrierName = new byte[24];
		carrierAlias = new byte[24];
		carrierOwnerOrg = 0;
		carrierOwnerName = new byte[24];
		carrierCapacity = 0;
	}

	@Override
	public String toString()
	{
		return "\t" + type + ", " + status + ", "
				+ (hasCarrier ? "Carrier" : "NoCarrier") + ", "
				+ new String(id) + ", " + new String(alias) + ", "
				+ carrierOwnerOrg + ", " + new String(carrierOwnerName) + ", "
				+ carrierCapacity + "\n";
	}

}