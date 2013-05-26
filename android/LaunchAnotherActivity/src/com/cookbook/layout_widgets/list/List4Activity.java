package com.cookbook.layout_widgets.list;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class List4Activity extends ListActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupView();
	}

	private void setupView()
	{
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		// 获取ContentResolver
		ContentResolver contentResolver = this.getContentResolver();
		// 查询数据，返回Cursor
		Cursor cur = contentResolver.query(uri, null, null, null, null);

		StringBuffer sb = new StringBuffer();
		if (cur.moveToFirst()) {
			Log.i("List4", "cur.moveToNext()");
			do {
				String id = cur.getString(cur.getColumnIndex(Contacts._ID));
				String name = cur.getString(cur
						.getColumnIndex(Contacts.DISPLAY_NAME));
				sb.append("id=").append(id).append(", name=").append(name);

				
				Cursor phones = contentResolver.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + id, null, null);
				
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// 添加Phone的信息
					sb.append(", phone=").append(phoneNumber);
				}
				sb.append(";");
				phones.close();

			} while (cur.moveToNext());

		}
		cur.close();

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, sb.toString().split(";")));
	}

}
