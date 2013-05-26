package com.navchina.bdi.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GPosition
{
	// CoordType
	public static final int BJ54 = 0X01;
	public static final int WGS84 = 0X02;

	// LocMode
	public static final int FIX_GPS = 0x00;
	public static final int FIX_BD1 = 0x01;
	public static final int FIX_BD2 = 0x02;
	public static final int FIX_GALILEO = 0x03;
	public static final int FIX_GLONASS = 0x04;
	public static final int FIX_GPSONE = 0x0A;
	public static final int FIX_ANY = 0x10;
	public static final int FIX_OTHER = 0x21;

	// LocStatus
	public static final int FIXED = 0x01;
	public static final int UNFIXED = 0x02;

	// PointType
	public static final int UNPROCESS = 0x00;
	public static final int GOODP = 0x01;
	public static final int FLYP = 0x02;

	private long ts;
	private double lon;
	private double lat;
	private double speed;
	private int course;
	private double turnRate;
	private int alt;
	private int locMode;
	private int locStatus;
	private int coordType;
	private int pType;

	public long getTs()
	{
		return ts;
	}

	public void setTs(long ts)
	{
		this.ts = ts;
	}

	public double getLon()
	{
		return lon;
	}

	public void setLon(double lon)
	{
		this.lon = lon;
	}

	public double getLat()
	{
		return lat;
	}

	public void setLat(double lat)
	{
		this.lat = lat;
	}

	public double getSpeed()
	{
		return speed;
	}

	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	public int getCourse()
	{
		return course;
	}

	public void setCourse(int course)
	{
		this.course = course;
	}

	public double getTurnRate()
	{
		return turnRate;
	}

	public void setTurnRate(double turnRate)
	{
		this.turnRate = turnRate;
	}

	public int getAlt()
	{
		return alt;
	}

	public void setAlt(int alt)
	{
		this.alt = alt;
	}

	public int getLocMode()
	{
		return locMode;
	}

	public void setLocMode(int locMode)
	{
		this.locMode = locMode;
	}

	public int getLocStatus()
	{
		return locStatus;
	}

	public void setLocStatus(int locStatus)
	{
		this.locStatus = locStatus;
	}

	public int getCoordType()
	{
		return coordType;
	}

	public void setCoordType(int coordType)
	{
		this.coordType = coordType;
	}

	public int getpType()
	{
		return pType;
	}

	public void setpType(int pType)
	{
		this.pType = pType;
	}

	public GPosition()
	{
		ts = 0;
		lon = 0.0;
		lat = 0.0;
		speed = 0.0;
		course = 0;
		turnRate = 0.0;
		alt = 0;

		locMode = -1;
		locStatus = -1;
		coordType = -1;
		pType = -1;
	}

	public boolean vaild()
	{
		return (coordType == GPosition.BJ54 || coordType == GPosition.WGS84);
	}

	@Override
	public String toString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String t_str = sdf.format(new Date(ts * 1000));

		String locMode_str = "";
		switch (locMode) {
			case FIX_GPS:
				locMode_str = "GPS";
				break;
			case FIX_BD1:
				locMode_str = "BD1";
				break;
			case FIX_BD2:
				locMode_str = "BD2";
				break;
			case FIX_GALILEO:
				locMode_str = "GALILEO";
				break;
			case FIX_GLONASS:
				locMode_str = "GLONASS";
				break;
			case FIX_GPSONE:
				locMode_str = "GPSOne";
				break;
			case FIX_ANY:
				locMode_str = "ANY";
				break;
			case FIX_OTHER:
				locMode_str = "OTHER";
				break;
			default:
				locMode_str = "LocMode:" + locMode;
				break;
		}
		return "\t" + t_str + "[" + locMode_str + ", "
				+ String.format("%.05f", lon) + ", "
				+ String.format("%.05f", lat) + ", "
				+ String.format("%.02f", speed) + "km/h, " + course + "deg, "
				+ turnRate + "deg/s, " + alt + "m]" + "\n";
	}

	public static void main(String[] args)
	{
		GPosition pos = new GPosition();
		pos.setTs(new Date().getTime() / 1000);
		pos.setLon(116.1234567);
		pos.setLat(40.1234567);
		pos.setSpeed(115.9);
		pos.setCourse(45);
		pos.setTurnRate(-112.5);
		pos.setAlt(-100);
		pos.setLocMode(GPosition.FIX_BD1);
		pos.setLocStatus(GPosition.FIXED);
		pos.setCoordType(GPosition.BJ54);
		pos.setpType(GPosition.UNPROCESS);
		System.out.println(pos);
	}

}
