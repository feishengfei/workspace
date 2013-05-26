package com.navchina.bdi.pojo;

public class BDTermStatus
{
	private int power;
	private int sat1;
	private int sat2;
	private int cellSignal;

	public int getPower()
	{
		return power;
	}

	public void setPower(int power)
	{
		this.power = power;
	}

	public int getSat1()
	{
		return sat1;
	}

	public void setSat1(int sat1)
	{
		this.sat1 = sat1;
	}

	public int getSat2()
	{
		return sat2;
	}

	public void setSat2(int sat2)
	{
		this.sat2 = sat2;
	}

	public int getCellSignal()
	{
		return cellSignal;
	}

	public void setCellSignal(int cellSignal)
	{
		this.cellSignal = cellSignal;
	}

	public BDTermStatus()
	{
		power = -1;
		sat1 = 0;
		sat2 = 0;
		cellSignal = 99;
	}

	@Override
	public String toString()
	{
		String powerStr = " ";
		if(1<= power && power <= 100)
			powerStr = "cell:"+ power;
		else if( 0xff == power )
			powerStr = "DC Power";
		return "\t" + powerStr + "sat1:" + sat1 + ", " + "sat2:" + sat2 + "\n";
	}

}
