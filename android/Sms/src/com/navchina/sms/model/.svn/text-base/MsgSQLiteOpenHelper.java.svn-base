package com.navchina.sms.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MsgSQLiteOpenHelper extends SQLiteOpenHelper
{
	public static final int VERSION = 1;
	public static final String DB_NAME = "bdmsg_db.sqlite";

	// table bdmsg
	public static final String BDMSG_TABLE = "bdmsg";
	public static final String BDMSG_ID = "_id";
	public static final String BDMSG_NUM = "num";
	public static final String BDMSG_CONTENT = "content";
	public static final String BDMSG_TYPE = "type";
	public static final String BDMSG_TIME = "time";
	public static final String BDMSG_SERIALNUM = "serialnum";
	public static final String BDMSG_LON = "lon";
	public static final String BDMSG_LAT = "lat";
	public static final String BDMSG_INDEX_TYPE = BDMSG_TABLE + "_" + BDMSG_TYPE;
	
	//bdmsg type
	public static final int TYPE_UNSEND = 0;
	public static final int TYPE_SENT = 1;
	public static final int TYPE_READ = 2;
	public static final int TYPE_UNREAD = 3;
	public static final int TYPE_DRAFT = 4;

	// table servicemsg
	public static final String SERVMSG_TABLE = "servicemsg";
	public static final String SERVMSG_ID = "_id";
	public static final String SERVMSG_CONTENT = "content";
	public static final String SERVMSG_TIME = "time";

	public MsgSQLiteOpenHelper(Context context)
	{
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		createTableBDMsg(db);
		createTableServMsg(db);
	}

	private void createTableBDMsg(SQLiteDatabase db)
	{
		db.execSQL("create table " + BDMSG_TABLE + " (" + 
				BDMSG_ID + " integer primary key autoincrement, " + 
				BDMSG_NUM + " text not null, " + 
				BDMSG_CONTENT + " text not null, " +
				BDMSG_TYPE + " char not null, " + 
				BDMSG_TIME + " integer not null, " + 
				BDMSG_SERIALNUM + " integer not null, " + 
				BDMSG_LON + " integer, " + 
				BDMSG_LAT + " integer" + 
				");"
		);
		db.execSQL(
				"create index "  + BDMSG_INDEX_TYPE + " on " + 
				BDMSG_TABLE + "(" + BDMSG_TYPE + ");"
		);
		
	}

	private void createTableServMsg(SQLiteDatabase db)
	{
		db.execSQL("create table " + SERVMSG_TABLE + " (" + 
				BDMSG_ID + " integer primary key autoincrement, " + 
				BDMSG_NUM + " text not null, " + 
				BDMSG_CONTENT + " text not null, " +
				BDMSG_TYPE + " char not null, " + 
				BDMSG_TIME + " integer not null, " + 
				BDMSG_SERIALNUM + " integer not null, " + 
				BDMSG_LON + " integer, " + 
				BDMSG_LAT + " integer" + 
				");"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO
	}

}
