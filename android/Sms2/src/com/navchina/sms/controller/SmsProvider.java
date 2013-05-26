package com.navchina.sms.controller;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.navchina.sms.model.Sms;
import com.navchina.sms.model.Sms.BDMsgColumns;
import com.navchina.sms.model.Sms.ServMsgColumns;

public class SmsProvider extends ContentProvider
{
	private static final String TAG = "SmsProvider";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "bdmsg_db.sqlite";

	private static HashMap<String, String> sBDMsgProjectionMap;
	private static HashMap<String, String> sServMsgProjectionMap;

	private static final int BDMSGS = 1;
	private static final int BDMSG_ID = 2;
	private static final int SERVMSGS = 3;
	private static final int SERVMSG_ID = 4;
	private static final UriMatcher sUriMatcher;

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// bdmsg
			db.execSQL("CREATE TABLE " + BDMsgColumns.TABLE_NAME + " ("
					+ BDMsgColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ BDMsgColumns.NUM + " TEXT NOT NULL, "
					+ BDMsgColumns.CONTENT + " TEXT NOT NULL, "
					+ BDMsgColumns.TYPE + " CHAR NOT NULL, "
					+ BDMsgColumns.TIME + " INTEGER NOT NULL, "
					+ BDMsgColumns.SERIALNUM + " INTEGER NOT NULL, "
					+ BDMsgColumns.LON + " INTEGER, " + BDMsgColumns.LAT
					+ " INTEGER, " + ");");
			db.execSQL("CREATE INDEX " + BDMsgColumns.BDMSG_INDEX_TYPE + " on "
					+ BDMsgColumns.TABLE_NAME + "(" + BDMsgColumns.TYPE + ");");

			// servmsg
			db.execSQL("CREATE TABLE " + ServMsgColumns.TABLE_NAME + " ("
					+ ServMsgColumns._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ ServMsgColumns.CONTENT + " TEXT NOT NULL, "
					+ ServMsgColumns.TIME + " INTEGER, " + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + BDMsgColumns.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ServMsgColumns.TABLE_NAME);
			onCreate(db);
		}
	}

	private DatabaseHelper mOpenHelper;

	@Override
	public boolean onCreate()
	{
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(BDMsgColumns.TABLE_NAME);

		String orderBy;
		switch (sUriMatcher.match(uri)) {
			case BDMSGS:
				qb.setProjectionMap(sBDMsgProjectionMap);
				orderBy = BDMsgColumns.DEFAULT_SORT_ORDER;
				break;

			case BDMSG_ID:
				qb.setProjectionMap(sBDMsgProjectionMap);
				qb.appendWhere(BDMsgColumns._ID + "="
						+ uri.getPathSegments().get(1));
				orderBy = BDMsgColumns.DEFAULT_SORT_ORDER;
				break;

			case SERVMSGS:
				qb.setProjectionMap(sServMsgProjectionMap);
				orderBy = ServMsgColumns.DEFAULT_SORT_ORDER;
				break;

			case SERVMSG_ID:
				qb.setProjectionMap(sServMsgProjectionMap);
				qb.appendWhere(ServMsgColumns._ID + "="
						+ uri.getPathSegments().get(1));
				orderBy = ServMsgColumns.DEFAULT_SORT_ORDER;
				break;

			default:
				throw new IllegalArgumentException("Unknown Uri:" + uri);
		}

		if (!TextUtils.isEmpty(sortOrder)) {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, orderBy);

		// Tell the cursor what uri to watch, so it knows when its source data
		// changes
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri)
	{
		switch (sUriMatcher.match(uri)) {
			case BDMSGS:
				return BDMsgColumns.CONTENT_TYPE;
			case BDMSG_ID:
				return BDMsgColumns.CONTENT_ITEM_TYPE;
			case SERVMSGS:
				return BDMsgColumns.CONTENT_TYPE;
			case SERVMSG_ID:
				return BDMsgColumns.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("Unknown Uri:" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		if(sUriMatcher.match(uri)!= BDMSGS || sUriMatcher.match(uri)!= SERVMSGS){
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Sms.AUTHORITH, BDMsgColumns.TABLE_NAME, BDMSGS);
		sUriMatcher.addURI(Sms.AUTHORITH, BDMsgColumns.TABLE_NAME + "/#",
				BDMSG_ID);
		sUriMatcher.addURI(Sms.AUTHORITH, ServMsgColumns.TABLE_NAME, SERVMSGS);
		sUriMatcher.addURI(Sms.AUTHORITH, ServMsgColumns.TABLE_NAME + "/#",
				SERVMSG_ID);

		sBDMsgProjectionMap = new HashMap<String, String>();
		sBDMsgProjectionMap.put(BDMsgColumns._ID, BDMsgColumns._ID);
		sBDMsgProjectionMap.put(BDMsgColumns.NUM, BDMsgColumns.NUM);
		sBDMsgProjectionMap.put(BDMsgColumns.CONTENT, BDMsgColumns.CONTENT);
		sBDMsgProjectionMap.put(BDMsgColumns.TYPE, BDMsgColumns.TYPE);
		sBDMsgProjectionMap.put(BDMsgColumns.TIME, BDMsgColumns.TIME);
		sBDMsgProjectionMap.put(BDMsgColumns.SERIALNUM, BDMsgColumns.SERIALNUM);
		sBDMsgProjectionMap.put(BDMsgColumns.LON, BDMsgColumns.LON);
		sBDMsgProjectionMap.put(BDMsgColumns.LAT, BDMsgColumns.LAT);

		sServMsgProjectionMap = new HashMap<String, String>();
		sServMsgProjectionMap.put(ServMsgColumns._ID, ServMsgColumns._ID);
		sServMsgProjectionMap.put(ServMsgColumns.CONTENT,
				ServMsgColumns.CONTENT);
		sServMsgProjectionMap.put(ServMsgColumns.TIME, ServMsgColumns.TIME);

	}
}
