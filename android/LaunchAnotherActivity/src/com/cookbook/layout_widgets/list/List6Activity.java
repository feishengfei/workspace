package com.cookbook.layout_widgets.list;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class List6Activity extends ListActivity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		Cursor cur = getContentResolver().query(uri, null, null, null,
				null);

		ArrayList<String> curList = new ArrayList<String>();

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				curList.add(cur.getColumnName(0));
				curList.add(cur.getColumnName(1));
				curList.add(cur.getColumnName(2));
				curList.add(cur.getColumnName(3));
				curList.add(cur.getColumnName(4));
				curList.add(cur.getColumnName(5));
				curList.add(cur.getColumnName(6));
				curList.add(cur.getColumnName(7));
				curList.add(cur.getColumnName(8));
				curList.add(cur.getColumnName(9));
				curList.add(cur.getColumnName(10));
				curList.add(cur.getColumnName(11));
				curList.add(cur.getColumnName(12));
				curList.add(cur.getColumnName(13));
				curList.add(cur.getColumnName(14));
			}
		}

		// binding the data to ListView
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, curList));
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

	}

}
