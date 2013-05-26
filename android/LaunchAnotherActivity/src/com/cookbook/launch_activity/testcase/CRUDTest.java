package com.cookbook.launch_activity.testcase;

import java.util.ArrayList;
import java.util.Random;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.test.AndroidTestCase;
import android.util.Log;

public class CRUDTest extends AndroidTestCase
{
	private static final String TAG = "CRUDTest";
	private static final String BD_PHONE_LABEL = "北斗终端";

	public void test4RetrieveContact()
	{
		ContentResolver contentResolver = getContext().getContentResolver();

		// 获得所有的联系人
		Cursor cursor = contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		// 循环遍历
		if (cursor.moveToFirst()) {
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			do {
				// 获得联系人的ID号
				String contactId = cursor.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = cursor.getString(displayNameColumn);
				Log.i(TAG, "联系人姓名: " + disPlayName);

				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cursor
						.getInt(cursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					// 获得联系人的电话号码列表
					Cursor phonesCursor = getContext()
							.getContentResolver()
							.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
									null,
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID
											+ " = " + contactId, null, null);
					if (phonesCursor.moveToFirst()) {
						do {
							// 遍历所有的电话号码
							String phoneNumber = phonesCursor
									.getString(phonesCursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							Log.i(TAG, "联系人电话: " + phoneNumber);
						} while (phonesCursor.moveToNext());
					}
					phonesCursor.close();
				}

				// 获得联系人的EMAIL
				Cursor emailCursor = getContext().getContentResolver().query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Email.CONTACT_ID
								+ " = " + contactId, null, null);
				if (emailCursor.moveToFirst()) {
					do {
						// 遍历所有的email
						String email = emailCursor
								.getString(emailCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
						Log.i(TAG, "Email: " + email);
					} while (emailCursor.moveToNext());
				}
				emailCursor.close();

				// 获得邮编等信息
				Cursor postalCursor = getContext()
						.getContentResolver()
						.query(
						// ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
						Data.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID
										+ " = "
										+ contactId
										+ " AND "
										+ Data.MIMETYPE
										+ "='"
										+ StructuredPostal.CONTENT_ITEM_TYPE
										+ "'", null, null);
				if (postalCursor.moveToFirst()) {
					do {
						String country = postalCursor
								.getString(postalCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
						String city = postalCursor
								.getString(postalCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
						String street = postalCursor
								.getString(postalCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
						String postcode = postalCursor
								.getString(postalCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
						Log.i(TAG, "国家: " + country + "\n城市: " + city
								+ "\n街道: " + street + "\n邮政编码: " + postcode);
					} while (postalCursor.moveToNext());
				}
				postalCursor.close();

				// 获得IM
				Cursor imCursor = getContext()
						.getContentResolver()
						.query(Data.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Im.CONTACT_ID
										+ "="
										+ contactId
										+ " AND "
										+ ContactsContract.Data.MIMETYPE
										+ "='"
										+ ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE
										+ "'", null, null);
				if (imCursor.moveToFirst()) {
					do {
						String IM = imCursor
								.getString(imCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
						int type = imCursor
								.getInt(imCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
						switch (type) {
							case -1:
								Log.i(TAG, "Custom: " + IM);
								break;
							case 0:
								Log.i(TAG, "AIM: " + IM);
								break;
							case 1:
								Log.i(TAG, "MSN: " + IM);
								break;
							case 2:
								Log.i(TAG, "YAHOO: " + IM);
								break;
							case 3:
								Log.i(TAG, "SKYPE: " + IM);
								break;
							case 4:
								Log.i(TAG, "QQ: " + IM);
								break;
							case 5:
								Log.i(TAG, "GoogleTalk: " + IM);
								break;
							case 6:
								Log.i(TAG, "ICQ: " + IM);
								break;
							case 7:
								Log.i(TAG, "JABBER: " + IM);
								break;
							case 8:
								Log.i(TAG, "NETMEETING: " + IM);
								break;
						}
					} while (imCursor.moveToNext());
				}
				imCursor.close();

				// 获取website
				Cursor websiteCursor = getContext()
						.getContentResolver()
						.query(ContactsContract.Data.CONTENT_URI,
								new String[] { ContactsContract.CommonDataKinds.Website.URL },
								ContactsContract.CommonDataKinds.Website.CONTACT_ID
										+ " = "
										+ contactId
										+ " AND "
										+ ContactsContract.Data.MIMETYPE
										+ "='"
										+ ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
										+ "'", null, null);
				if (websiteCursor.moveToFirst()) {
					do {
						String website = websiteCursor
								.getString(websiteCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
						Log.i(TAG, "个人网站: " + website);
					} while (websiteCursor.moveToNext());
				}
				websiteCursor.close();

				// 获得note
				Cursor noteCursor = getContext()
						.getContentResolver()
						.query(ContactsContract.Data.CONTENT_URI,
								new String[] { ContactsContract.CommonDataKinds.Note.NOTE },
								ContactsContract.CommonDataKinds.Note.CONTACT_ID
										+ " = "
										+ contactId
										+ " AND "
										+ ContactsContract.Data.MIMETYPE
										+ "='"
										+ ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE
										+ "'", null, null);
				if (noteCursor.moveToFirst()) {
					String note = noteCursor
							.getString(noteCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
					Log.i(TAG, "备注: " + note);
				}
				noteCursor.close();

				// 获得nickname
				Cursor nicknameCursor = getContext()
						.getContentResolver()
						.query(ContactsContract.Data.CONTENT_URI,
								new String[] { ContactsContract.CommonDataKinds.Nickname.NAME },
								ContactsContract.CommonDataKinds.Nickname.CONTACT_ID
										+ "="
										+ contactId
										+ " AND "
										+ ContactsContract.Data.MIMETYPE
										+ "='"
										+ ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE
										+ "'", null, null);
				if (nicknameCursor.moveToFirst()) {
					String nickname = nicknameCursor
							.getString(nicknameCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));
					Log.i(TAG, "昵称: " + nickname);
				}
				nicknameCursor.close();

				// 获得organization
				Cursor orgCursor = getContext()
						.getContentResolver()
						.query(ContactsContract.Data.CONTENT_URI,
								new String[] {
										ContactsContract.CommonDataKinds.Organization.COMPANY,
										ContactsContract.CommonDataKinds.Organization.TITLE },
								ContactsContract.CommonDataKinds.Nickname.CONTACT_ID
										+ "="
										+ contactId
										+ " AND "
										+ ContactsContract.Data.MIMETYPE
										+ "='"
										+ ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
										+ "'", null, null);
				if (orgCursor.moveToFirst()) {
					do {
						String company = orgCursor
								.getString(orgCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
						String position = orgCursor
								.getString(orgCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
						Log.i(TAG, "公司: " + company + "\n职位: " + position);
					} while (orgCursor.moveToNext());
				}
				orgCursor.close();
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	public void test1AddContact()
	{
		String name = "运营中心";
		Random rand = new Random();
		StringBuilder numSb = new StringBuilder();

		for (int i = 0; i < 20; i++) {
			numSb.setLength(0);
			for (int j = 0; j < 6; j++) {
				numSb.append(rand.nextInt(10));
			}
			addContact(name + i, numSb.toString());
			Log.i(TAG, "add:" + name + i + ", " + numSb.toString());
		}
	}

	public void test2DeleteContact()
	{
		for (int i = 0; i < 10; i++) {
			String name = "运营中心";
			deleteContact(name + i);
			Log.i(TAG, "delete:" + name + i);
		}
		deleteContact("远洋中心");
	}

	public void test3UpdateContact()
	{
		String oldName = "运营中心9";
		String name = "远洋中心";
		String number = "123456";
		updateContact(oldName, name, number);
	}

	private void deleteContact(String name)
	{
		Cursor cursor = getContext().getContentResolver().query(
				Data.CONTENT_URI, new String[] { Data.RAW_CONTACT_ID },
				ContactsContract.Contacts.DISPLAY_NAME + "=?",
				new String[] { name }, null);

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		if (cursor.moveToFirst()) {
			do {
				long Id = cursor.getLong(cursor
						.getColumnIndex(Data.RAW_CONTACT_ID));
				ops.add(ContentProviderOperation
						.newDelete(
								ContentUris.withAppendedId(
										RawContacts.CONTENT_URI, Id)).build());
				try {
					getContext().getContentResolver().applyBatch(
							ContactsContract.AUTHORITY, ops);
				}
				catch (Exception e) {
				}
			} while (cursor.moveToNext());
			cursor.close();
		}
	}

	private void addContact(String name, String number)
	{
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();

		// add RawContacts
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());

		// add Name
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, name).build());

		// add Number
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
				.withValue(Phone.TYPE, Phone.TYPE_CUSTOM)
				.withValue(Phone.LABEL, BD_PHONE_LABEL)
				.withValue(Phone.NUMBER, number).build());

		try {
			getContext().getContentResolver().applyBatch(
					ContactsContract.AUTHORITY, ops);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (OperationApplicationException e) {
			e.printStackTrace();
		}
	}

	private void updateContact(String oldname, String name, String number)
	{
		Cursor cursor = getContext().getContentResolver().query(
				Data.CONTENT_URI, new String[] { Data.RAW_CONTACT_ID },
				ContactsContract.Contacts.DISPLAY_NAME + "=?",
				new String[] { oldname }, null);
		cursor.moveToFirst();
		String id = cursor
				.getString(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
		cursor.close();

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		// 更新姓名
		ops.add(ContentProviderOperation
				.newUpdate(ContactsContract.Data.CONTENT_URI)
				.withSelection(
						Data.RAW_CONTACT_ID + "=?" + " AND "
								+ ContactsContract.Data.MIMETYPE + " = ?",
						new String[] { String.valueOf(id),
								StructuredName.CONTENT_ITEM_TYPE })
				.withValue(StructuredName.DISPLAY_NAME, name).build());

		// 更新电话号码
		ops.add(ContentProviderOperation
				.newUpdate(ContactsContract.Data.CONTENT_URI)
				.withSelection(
						Data.RAW_CONTACT_ID + "=?" + " AND "
								+ ContactsContract.Data.MIMETYPE + " = ?"
								+ " AND " + Phone.TYPE + "=?",
						new String[] { String.valueOf(id),
								Phone.CONTENT_ITEM_TYPE,
								String.valueOf(Phone.TYPE_CUSTOM) })
				.withValue(Phone.NUMBER, number).build());

		try {
			getContext().getContentResolver().applyBatch(
					ContactsContract.AUTHORITY, ops);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
