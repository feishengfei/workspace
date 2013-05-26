package com.navchina.contact;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.navchina.R;

public class ContactsActivity extends ListActivity
{
	private static final String TAG = "contacts:";
	private static final int ID_CONTACTS_EDIT = 1;
	private static final int ID_CONTACTS_DELETE = 2;
	private static final int DIALOG_CONTACTS_EDIT = 1;
	private static final int DIALOG_CONTACTS_ADD = 3;
	private static final int GROUP_DEFAULT = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupView();
	}

	private void setupView()
	{
		// step 1:查询联系人
		Cursor cursor = managedQuery(Data.CONTENT_URI, null,
		/* Contacts.HAS_PHONE_NUMBER + " = 1" + " AND " + */
		Data.MIMETYPE + " ='" + Phone.CONTENT_ITEM_TYPE + "'", null,
				Data.DISPLAY_NAME + " ASC");
		String[] fromColumns = new String[] { Contacts.DISPLAY_NAME,
				Phone.NUMBER };
		int[] toLayoutIDs = new int[] { android.R.id.text1, android.R.id.text2 };

		SimpleCursorAdapter myAdapter;
		myAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, cursor, fromColumns,
				toLayoutIDs);
		setListAdapter(myAdapter);

		// step 2:注册长按联系人后的菜单
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, ID_CONTACTS_EDIT, 0, R.string.contacts_edit);
		menu.add(0, ID_CONTACTS_DELETE, 0, R.string.contacts_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		}
		catch (ClassCastException e) {
			Log.e(TAG, "bad menuInfo", e);
			return super.onContextItemSelected(item);
		}

		long id = getListAdapter().getItemId(info.position);
		switch (item.getItemId()) {
			case ID_CONTACTS_EDIT:
				return onContactsEdit(id);
			case ID_CONTACTS_DELETE:
				return onContactsDelete(id);
		}
		return super.onContextItemSelected(item);
	}

	/*
	 * 删除某个联系人
	 */
	private boolean onContactsDelete(long id)
	{
		boolean ret = true;

		Cursor cursor = getContentResolver().query(
				ContentUris.withAppendedId(Data.CONTENT_URI, id),
				new String[] { Data.RAW_CONTACT_ID }, null, null, null);

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
					getContentResolver().applyBatch(ContactsContract.AUTHORITY,
							ops);
					Log.i(TAG, "delele id:" + id);
				}
				catch (Exception e) {
				}
			} while (cursor.moveToNext());
			cursor.close();
		}
		else {
			ret = false;
		}
		return ret;
	}

	/*
	 * 编辑某个联系人
	 */
	private boolean onContactsEdit(long id)
	{
		String oldname = "";
		String number = "";
		boolean ret = true;

		Cursor cursor = getContentResolver().query(
				ContentUris.withAppendedId(Data.CONTENT_URI, id), null, null,
				null, null);
		if (cursor.moveToNext()) {
			oldname = cursor
					.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
			number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
		}

		Intent intent = new Intent(this, EditContactActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("oldname", oldname);
		intent.putExtra("number", number);
		startActivityForResult(intent, DIALOG_CONTACTS_EDIT);
		return ret;
	}

	private void onContactsEditFinished(long id, String oldname, String name,
			String number)
	{
		String raw_id = "";
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		Cursor dataCursor = getContentResolver().query(
				ContentUris.withAppendedId(Data.CONTENT_URI, id), null, null,
				null, null);
		if (dataCursor.getCount() > 0) {
			if (dataCursor.moveToFirst()) {
				raw_id = dataCursor.getString(dataCursor
						.getColumnIndex(Data.RAW_CONTACT_ID));
			}
			else {
				Log.e(TAG, "Error on locate id 1:" + id);
			}
		}
		else {
			Log.e(TAG, "Error on locate id 2:" + id);
			return;
		}

		// 更新姓名
		ops.add(ContentProviderOperation
				.newUpdate(ContactsContract.Data.CONTENT_URI)
				.withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?",
						new String[] { raw_id })
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
						name).build());

		// 更新电话号码
		ops.add(ContentProviderOperation
				.newUpdate(ContentUris.withAppendedId(Data.CONTENT_URI, id))
				.withValue(Phone.NUMBER, number).build());

		try {
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (Exception e) {
			Log.e(TAG, "Error on Update", e);
		}
	}

	private void addContact(String name, String number)
	{
		int type = Phone.TYPE_HOME;
		String custom_type = "北斗终端";

		if (number.length() < 6) {
			type = Phone.TYPE_HOME;
		}
		else if (number.length() < 8) {
			type = Phone.TYPE_CUSTOM;
		}
		else {
			type = Phone.TYPE_MOBILE;
		}

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
				.withValue(Phone.TYPE, type)
				.withValue(Phone.LABEL, custom_type)
				.withValue(Phone.NUMBER, number).build());

		try {
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (RemoteException e) {
			Log.e(TAG, "Error on Add1", e);
		}
		catch (OperationApplicationException e) {
			Log.e(TAG, "Error on Add2", e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == DIALOG_CONTACTS_EDIT && resultCode == RESULT_OK) {
			long id = data.getLongExtra("id", 0);
			String oldname = data.getStringExtra("oldname");
			String name = data.getStringExtra("name");
			String number = data.getStringExtra("number");
			onContactsEditFinished(id, oldname, name, number);
			Log.i(TAG, "update:id = " + id + ", oldname = " + oldname
					+ ", name = " + name + ", number = " + number);
		}
		else if (requestCode == DIALOG_CONTACTS_ADD && resultCode == RESULT_OK) {
			String name = data.getStringExtra("name");
			String number = data.getStringExtra("number");
			addContact(name, number);
			Log.i(TAG, "add:name = " + name + ", number = " + number);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(GROUP_DEFAULT, DIALOG_CONTACTS_ADD, 0, R.string.contacts_add)
				.setIcon(android.R.drawable.ic_menu_add);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case DIALOG_CONTACTS_ADD:
				Intent intent = new Intent(this, EditContactActivity.class);
				intent.putExtra("id", "0");
				intent.putExtra("oldname", "");
				intent.putExtra("number", "");
				startActivityForResult(intent, DIALOG_CONTACTS_ADD);
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		// TODO 前往写短信界面
	}

}
