package com.navchina.test;

import static com.navchina.sms.model.MsgSQLiteOpenHelper.BDMSG_CONTENT;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.BDMSG_ID;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.BDMSG_LAT;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.BDMSG_LON;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.BDMSG_NUM;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.BDMSG_SERIALNUM;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.BDMSG_TABLE;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.BDMSG_TIME;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.BDMSG_TYPE;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.TYPE_DRAFT;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.TYPE_READ;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.TYPE_SENT;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.TYPE_UNREAD;
import static com.navchina.sms.model.MsgSQLiteOpenHelper.TYPE_UNSEND;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.navchina.R;
import com.navchina.sms.model.BDMsg;
import com.navchina.sms.model.MsgSQLiteOpenHelper;

public class TestDB extends AndroidTestCase
{
	private static final String TAG = "TestDB";
	private static int gSerialNum = 0;
	private SQLiteDatabase database;
	private Random rand;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		MsgSQLiteOpenHelper helper = new MsgSQLiteOpenHelper(getContext());
		database = helper.getWritableDatabase();
		rand = new Random();

	}

	public void testAddMsg()
	{
		Log.i(TAG, TestDB.class.getName() + ":testAddMsg()");
		for (int i = 0; i < 50; i++) {
			BDMsg msg = genBDMsg(rand);
			Log.i(TAG, msg.toString());
			addBDMsg(msg);
		}
		Log.i(TAG, "\n\n\n");
	}

	public void testLoadMsg()
	{
		ArrayList<BDMsg> msgs;
		int type = 0;

		type = TYPE_DRAFT;
		msgs = loadBDMsg(type);
		Log.i(TAG, TestDB.class.getName() + ":testLoadMsg:" + type);
		Iterator<BDMsg> it = msgs.iterator();
		while (it.hasNext()) {
			BDMsg bdMsg = (BDMsg) it.next();
			Log.i(TAG, bdMsg.toString());
		}

		type = TYPE_READ;
		msgs = loadBDMsg(type);
		Log.i(TAG, TestDB.class.getName() + ":testLoadMsg:" + type);
		it = msgs.iterator();
		while (it.hasNext()) {
			BDMsg bdMsg = (BDMsg) it.next();
			Log.i(TAG, bdMsg.toString());
		}

		type = TYPE_UNREAD;
		msgs = loadBDMsg(type);
		Log.i(TAG, TestDB.class.getName() + ":testLoadMsg:" + type);
		it = msgs.iterator();
		while (it.hasNext()) {
			BDMsg bdMsg = (BDMsg) it.next();
			Log.i(TAG, bdMsg.toString());
		}

		type = TYPE_SENT;
		msgs = loadBDMsg(type);
		Log.i(TAG, TestDB.class.getName() + ":testLoadMsg:" + type);
		it = msgs.iterator();
		while (it.hasNext()) {
			BDMsg bdMsg = (BDMsg) it.next();
			Log.i(TAG, bdMsg.toString());
		}

		type = TYPE_UNSEND;
		msgs = loadBDMsg(type);
		Log.i(TAG, TestDB.class.getName() + ":testLoadMsg:" + type);
		it = msgs.iterator();
		while (it.hasNext()) {
			BDMsg bdMsg = (BDMsg) it.next();
			Log.i(TAG, bdMsg.toString());
		}
		Log.i(TAG, "\n\n");
	}

	private void addBDMsg(BDMsg msg)
	{
		if (null == msg)
			return;

		ContentValues values = new ContentValues();
		values.put(BDMSG_NUM, msg.getNum());
		values.put(BDMSG_CONTENT, msg.getContent());
		values.put(BDMSG_TYPE, msg.getType());
		values.put(BDMSG_TIME, msg.getTime());
		values.put(BDMSG_SERIALNUM, msg.getSerialnum());

		if (msg.isPoint()) {
			values.put(BDMSG_LON, msg.getLon());
			values.put(BDMSG_LAT, msg.getLat());
		}
		msg.setId(database.insert(BDMSG_TABLE, null, values));

		// TODO
		switch (msg.getType()) {
			case TYPE_DRAFT:
			case TYPE_READ:
			case TYPE_UNREAD:
			case TYPE_SENT:
			case TYPE_UNSEND:
				break;
			default:
				break;
		}
	}

	private BDMsg genBDMsg(Random rand)
	{
		BDMsg ret;
		
		String content = "";
		String[] sms_mock = getContext().getResources().getStringArray(R.array.sms_mock);
		content = sms_mock[rand.nextInt(sms_mock.length)];
		
		String num = "";
		for (int i = 0; i < 6; i++) {
			num += rand.nextInt(10);
		}
		int type = rand.nextInt(5);
		long time;
		time = (new Date().getTime()) / 1000 - rand.nextInt(2* 365 * 24 * 3600);

		long serialnum = nextSN();
		boolean isPoint = false;
		ret = new BDMsg(num, content, type, time, serialnum, isPoint);
		return ret;
	}

	public static long nextSN()
	{
		return gSerialNum++;
	}

	private ArrayList<BDMsg> loadBDMsg(int type)
	{
		switch (type) {
			case TYPE_DRAFT:
			case TYPE_READ:
			case TYPE_UNREAD:
			case TYPE_SENT:
			case TYPE_UNSEND:
				break;
			default:
				return null;
		}

		ArrayList<BDMsg> ret = new ArrayList<BDMsg>();
		Cursor cursor = database.query(BDMSG_TABLE, null, BDMSG_TYPE + " = ?",
				new String[] { String.valueOf(type) }, null, null, BDMSG_TIME
						+ " ASC");
		if (cursor.getCount() > 0) {
			BDMsg msg;
			while (cursor.moveToNext()) {
				long id = cursor.getLong(cursor.getColumnIndex(BDMSG_ID));
				String num = cursor.getString(cursor.getColumnIndex(BDMSG_NUM));
				String content = cursor.getString(cursor
						.getColumnIndex(BDMSG_CONTENT));
				long time = cursor.getLong(cursor.getColumnIndex(BDMSG_TIME));
				long serialnum = cursor.getLong(cursor
						.getColumnIndex(BDMSG_SERIALNUM));
				boolean isPoint = !cursor.isNull(cursor
						.getColumnIndex(BDMSG_LON));
				msg = new BDMsg(num, content, type, time, serialnum, isPoint);
				msg.setId(id);
				if (isPoint) {
					int lon = cursor.getInt(cursor.getColumnIndex(BDMSG_LON));
					int lat = cursor.getInt(cursor.getColumnIndex(BDMSG_LAT));
					msg.setLat(lat);
					msg.setLon(lon);
				}
				ret.add(msg);
			}
		}
		return ret;
	}
}
