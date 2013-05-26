package com.cookbook.launch_activity.testcase;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.test.AndroidTestCase;
import android.util.Log;

public class ContactTest extends AndroidTestCase
{
	private static final String TAG = "TestContact";

	public void testInsert()
	{
		ContentValues values = new ContentValues();

		/*
		 * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId
		 */
		Uri rawContactUri = this.getContext().getContentResolver()
				.insert(RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);

		// 往data表里写入姓名数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.GIVEN_NAME, "运营中心");
		values.put(StructuredName.DISPLAY_NAME, "北斗星通");
		getContext().getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		// 往data表里写入电话数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.TYPE, Phone.TYPE_CUSTOM);
		values.put(Phone.LABEL, "北斗终端");
		values.put(Phone.DATA, "455712");
		getContext().getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		// 往data表里写入Email的数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		values.put(Email.TYPE, Email.TYPE_MOBILE);
		values.put(Email.DATA, "everyone@navchina.com");
		getContext().getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		// 往data表写入事件
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Event.CONTENT_ITEM_TYPE);
		values.put(Event.TYPE, Event.TYPE_BIRTHDAY);
		values.put(Event.DATA, "2012-5-4");
		getContext().getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		// 往data表写入Im
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
		values.put(Im.TYPE, Im.TYPE_HOME);
		values.put(Im.PROTOCOL, Im.PROTOCOL_QQ);
		values.put(Im.DATA, "707497090");
		getContext().getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

	}

	/*
	 * 批量添加，处于用一个事务中
	 */
	public void testSave()
	{
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();

		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());

		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, "良辰").build());

		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
				.withValue(Phone.NUMBER, "1234567890")
				.withValue(Phone.TYPE, Phone.TYPE_MOBILE)
				.withValue(Phone.LABEL, "手机号").build());

		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
				.withValue(Email.DATA, "liangchen@itcast.cn")
				.withValue(Email.TYPE, Email.TYPE_WORK).build());
		ContentProviderResult[] results;
		try {
			results = this.getContext().getContentResolver()
					.applyBatch(ContactsContract.AUTHORITY, ops);
			for (ContentProviderResult result : results) {
				Log.i(TAG, result.uri.toString());
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (OperationApplicationException e) {
			e.printStackTrace();
		}
	}

	public void testReadAllContacts()
	{
		Cursor cursor = getContext().getContentResolver().query(
				ContactsContract.RawContacts.CONTENT_URI, null, null, null,
				null);
		int contactIdIndex = 0;
		int nameIndex = 0;

		if (cursor.getCount() > 0) {
			contactIdIndex = cursor
					.getColumnIndex(ContactsContract.Contacts._ID);
			nameIndex = cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		}
		while (cursor.moveToNext()) {
			String contactId = cursor.getString(contactIdIndex);
			String name = cursor.getString(nameIndex);
			Log.i(TAG, "id:" + contactId + ", " + "name:" + name);

			// phone
			Cursor phones = getContext().getContentResolver().query(
					Phone.CONTENT_URI, null,
					Phone.CONTACT_ID + "=" + contactId, null, null);
			int phoneIndex = 0;
			if (phones.getCount() > 0) {
				phoneIndex = phones.getColumnIndex(Phone.DATA);
			}
			while (phones.moveToNext()) {
				String phoneNumber = phones.getString(phoneIndex);
				Log.i(TAG, "phone:" + phoneNumber);
			}

			// email
			Cursor emails = getContext().getContentResolver().query(
					Email.CONTENT_URI, null,
					Email.CONTACT_ID + "=" + contactId, null, null);
			int emailIndex = 0;
			if (emails.getCount() > 0) {
				emailIndex = emails.getColumnIndex(Email.DATA1);
			}
			while (emails.moveToNext()) {
				String email = emails.getString(emailIndex);
				Log.i(TAG, "email:" + email);
			}
			Log.i(TAG, "");
		}
	}
}
