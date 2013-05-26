package com.cookbook.launch_activity.testcase;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.test.AndroidTestCase;
import android.util.Log;

public class TestInsertBD extends AndroidTestCase
{
	private static final String TAG = "TestInsertBD";
	private String name = "";
	private String number = "";
	private String label = "";

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		name = "运营中心";
		number = "455712";
		label = "北斗终端";
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	};

	public void testAdd()
	{
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();

		// add RawContacts
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());
		Log.i(TAG, "rawId" + rawContactInsertIndex);

		// add Name
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, name).build());
		Log.i(TAG, "rawId" + rawContactInsertIndex);

		// add Number
		ops.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
				.withValue(Phone.TYPE, Phone.TYPE_CUSTOM)
				.withValue(Phone.LABEL, label).withValue(Phone.NUMBER, number)
				.build());
		Log.i(TAG, "rawId" + rawContactInsertIndex);

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

	public void testQueryRawContact()
	{

		Cursor c = this.getContext().getContentResolver()
				.query(RawContacts.CONTENT_URI, null, null, null, null);
		while (c.moveToNext()) {
			Log.i(TAG,
					c.getString(c.getColumnIndex(RawContacts.ACCOUNT_NAME))
							+ ":"
							+ c.getString(c
									.getColumnIndex(RawContacts.ACCOUNT_TYPE)));
		}
	}

	public void testReadAllContacts()
	{
		Cursor cursor = getContext().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, null, null, null);
	}
}
