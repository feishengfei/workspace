package com.cookbook.launch_activity.db;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.cookbook.launch_activity.R;

public class ContactActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_add_contact);
		setupView();
	}

	private void setupView()
	{
		Button addBtn = (Button) findViewById(R.id.btn_add_contact);
		addBtn.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Uri insertUri = android.provider.ContactsContract.Contacts.CONTENT_URI;
				Intent intent = new Intent(Intent.ACTION_INSERT, insertUri);
				startActivity(intent);
			}
		});

		Button viewBtn = (Button) findViewById(R.id.btn_view);
		viewBtn.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Uri insertUri = android.provider.ContactsContract.Contacts.CONTENT_URI;
				Intent intent = new Intent(Intent.ACTION_VIEW, insertUri);
				startActivity(intent);
			}
		});

		Button queryBtn1 = (Button) findViewById(R.id.btn_query1);
		queryBtn1.setOnClickListener(query1);

		Button queryBtn2 = (Button) findViewById(R.id.btn_query2);
		queryBtn2.setOnClickListener(query2);
		
		Button queryBtn3 = (Button) findViewById(R.id.btn_query3);
		queryBtn3.setOnClickListener(query3);
		
		Button insertBtn1 = (Button)findViewById(R.id.btn_insert1);
		insertBtn1.setOnClickListener(insert1);
	}

	private OnClickListener query1 = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			StringBuilder sb = new StringBuilder();
			Uri queryUri = Contacts.CONTENT_URI;
			Cursor c = ContactActivity.this.getContentResolver().query(
					queryUri, null, null, null, null);
			if (c.getCount() > 0) {
				int i = 0;
				while (c.moveToNext()) {

					sb.append(c.getString(c.getColumnIndex(Contacts.STARRED)));

					if (++i % 8 != 0) {
						sb.append("\t");
					}
					else {
						sb.append("\n");
					}
				}
			}

			Toast.makeText(ContactActivity.this, sb.toString(),
					Toast.LENGTH_SHORT).show();
		}
	};

	private OnClickListener query2 = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			StringBuilder sb = new StringBuilder();
			Uri queryUri = Contacts.CONTENT_URI;
			Cursor c = getContentResolver().query(queryUri, null, null, null,
					null);
			if (c.getCount() > 0) {
				int i = 0;
				while (c.moveToNext()) {

					sb.append(c.getString(c
							.getColumnIndex(Contacts.DISPLAY_NAME)));

					if (++i % 8 != 0) {
						sb.append("\t");
					}
					else {
						sb.append("\n");
					}
				}
			}

			Toast.makeText(ContactActivity.this, sb.toString(),
					Toast.LENGTH_SHORT).show();
		}
	};

	private OnClickListener query3 = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode("15001399705"));
			Cursor c = getContentResolver()
					.query(uri, new String[] { PhoneLookup._ID, PhoneLookup.DISPLAY_NAME },
							null, null, null);
			StringBuilder sb = new StringBuilder();
			sb.append("count = " + c.getCount() + "\n");
			if (c.getCount() > 0) {
				int i = 0;
				while (c.moveToNext()) {

					sb.append("id= " + c.getString(c
							.getColumnIndex(PhoneLookup._ID)));
					sb.append("name= " + c.getString(c
							.getColumnIndex(PhoneLookup.DISPLAY_NAME)) + "\n");
				}
			}

			Toast.makeText(ContactActivity.this, sb.toString(),
					Toast.LENGTH_LONG).show();

		}
	};
	
	private OnClickListener insert1 = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			ContentValues values = new ContentValues();
			
			/* 
	         * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId 
	         */ 
			Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
			long rawContactId = ContentUris.parseId(rawContactUri);
			
			//往data表里写入姓名数据
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
			values.put(StructuredName.GIVEN_NAME, "运营中心");
			values.put(StructuredName.PREFIX, "先生");
			getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
			
			//往data表里写入电话数据
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
			values.put(Phone.NUMBER, "455712");
			values.put(Phone.TYPE, Phone.TYPE_CUSTOM);
			values.put(Phone.LABEL, "北斗终端");
			getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
			
			//往data表里写入Email的数据
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
			values.put(Email.TYPE, Email.TYPE_WORK);
			values.put(Email.DATA, "everyone@navchina.com");
			getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
			
		}
	};
}
