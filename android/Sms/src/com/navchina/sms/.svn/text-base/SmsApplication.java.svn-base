package com.navchina.sms;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.navchina.sms.model.BDMsg;
import com.navchina.sms.model.MsgSQLiteOpenHelper;

import static com.navchina.sms.model.MsgSQLiteOpenHelper.*;

public class SmsApplication extends Application
{
	private SQLiteDatabase database;
	private ArrayList<BDMsg> sentMsgs;
	private ArrayList<BDMsg> unsendMsgs;
	private ArrayList<BDMsg> inboxMsgs;
	private ArrayList<BDMsg> draftMsgs;

	@Override
	public void onCreate()
	{
		super.onCreate();
		MsgSQLiteOpenHelper helper = new MsgSQLiteOpenHelper(this);
		database = helper.getWritableDatabase();
		loadBDMsg();
	}

	private void loadBDMsg()
	{
		sentMsgs = loadBDMsg(TYPE_SENT);
		unsendMsgs = loadBDMsg(TYPE_UNSEND);
		inboxMsgs = loadBDMsg(TYPE_UNREAD, TYPE_READ);
		draftMsgs = loadBDMsg(TYPE_DRAFT);
	}

	private ArrayList<BDMsg> loadBDMsg(int type)
	{
		if (!isValidType(type))
			return null;

		ArrayList<BDMsg> ret = new ArrayList<BDMsg>();
		Cursor cursor = database.query(BDMSG_TABLE, null, BDMSG_TYPE + " = ?",
				new String[] { String.valueOf(type) }, null, null, BDMSG_TIME
						+ " DESC");
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
		cursor.close();
		return ret;
	}

	private ArrayList<BDMsg> loadBDMsg(int type1, int type2)
	{
		if (!(isValidType(type1) && isValidType(type2)))
			return null;

		ArrayList<BDMsg> ret = new ArrayList<BDMsg>();
		Cursor cursor = database.query(BDMSG_TABLE, null, BDMSG_TYPE + " = ?"
				+ " OR " + BDMSG_TYPE + " = ?",
				new String[] { String.valueOf(type1), String.valueOf(type2) },
				null, null, BDMSG_TIME + " DESC");

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
				int type = cursor.getInt(cursor.getColumnIndex(BDMSG_TYPE));

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
		cursor.close();
		return ret;
	}

	private int getTypeCount(int type)
	{
		int ret = 0;
		if (!isValidType(type))
			return 0;

		Cursor cursor = database.query(BDMSG_TABLE, null, BDMSG_TYPE + " = ?",
				new String[] { String.valueOf(type) }, null, null, null);
		ret = cursor.getCount();
		cursor.close();
		return ret;
	}

	private boolean isValidType(int type)
	{
		switch (type) {
			case TYPE_DRAFT:
			case TYPE_READ:
			case TYPE_UNREAD:
			case TYPE_SENT:
			case TYPE_UNSEND:
				return true;
			default:
				return false;
		}
	}

	public void addBDMsg(BDMsg msg)
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

		switch (msg.getType()) {
			case TYPE_DRAFT:
				draftMsgs.add(msg);
				break;
			case TYPE_READ:
			case TYPE_UNREAD:
				inboxMsgs.add(msg);
				break;
			case TYPE_SENT:
				sentMsgs.add(msg);
				break;
			case TYPE_UNSEND:
				unsendMsgs.add(msg);
				break;
			default:
				break;
		}
	}

	public void deleteMsgs(Long[] ids)
	{
		// database delete
		StringBuffer idList = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			idList.append(ids[i]);
			if (i < ids.length - 1) {
				idList.append(",");
			}
		}
		String where = String.format("%s in (%s)", BDMSG_ID, idList);
		database.delete(BDMSG_TABLE, where, null);

		// memeory delete
		for (Long id : ids) {
			deleteMsgFromMemory(id);
		}
	}

	public void deleteMsg(long id)
	{
		// database delete
		String where = String.format("%s = %ld", BDMSG_ID, id);
		database.delete(BDMSG_TABLE, where, null);

		// memory delete
		deleteMsgFromMemory(id);
	}

	public void updateMsg(BDMsg msg)
	{
		ContentValues values = new ContentValues();
		values.put(BDMSG_ID, msg.getId());
		values.put(BDMSG_NUM, msg.getNum());
		values.put(BDMSG_CONTENT, msg.getContent());
		values.put(BDMSG_TYPE, msg.getType());
		values.put(BDMSG_TIME, msg.getTime());
		values.put(BDMSG_SERIALNUM, msg.getSerialnum());
		values.put(BDMSG_SERIALNUM, msg.getSerialnum());
		values.put(BDMSG_SERIALNUM, msg.getSerialnum());
		
        long id = msg.getId();
        String where = String.format("%s = %s", BDMSG_ID, id);
		
		database.update(BDMSG_TABLE, values, where, null);
	}
	
	private void deleteMsgFromMemory(long id)
	{
		Iterator<BDMsg> it = sentMsgs.iterator();
		while (it.hasNext()) {
			BDMsg bdMsg = (BDMsg) it.next();
			if (id == bdMsg.getId()) {
				it.remove();
				return;
			}
		}

		it = unsendMsgs.iterator();
		while (it.hasNext()) {
			BDMsg bdMsg = (BDMsg) it.next();
			if (id == bdMsg.getId()) {
				it.remove();
				return;
			}
		}
		it = inboxMsgs.iterator();
		while (it.hasNext()) {
			BDMsg bdMsg = (BDMsg) it.next();
			if (id == bdMsg.getId()) {
				it.remove();
				return;
			}
		}
		it = draftMsgs.iterator();
		while (it.hasNext()) {
			BDMsg bdMsg = (BDMsg) it.next();
			if (id == bdMsg.getId()) {
				it.remove();
				return;
			}
		}
	}

	public int getUnreadCount()
	{
		return getTypeCount(TYPE_UNREAD);
	}

	public int getUnsendCount()
	{
		return getTypeCount(TYPE_UNSEND);
	}

	public ArrayList<BDMsg> getSentMsgs()
	{
		return sentMsgs;
	}

	public ArrayList<BDMsg> getUnsendMsgs()
	{
		return unsendMsgs;
	}

	public ArrayList<BDMsg> getInboxMsgs()
	{
		return inboxMsgs;
	}

	public ArrayList<BDMsg> getDraftMsgs()
	{
		return draftMsgs;
	}

}
