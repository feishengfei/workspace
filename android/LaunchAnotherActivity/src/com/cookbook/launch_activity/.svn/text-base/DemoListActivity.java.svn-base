package com.cookbook.launch_activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DemoListActivity extends ListActivity
{
	static final String[] ACTIVITY_CHOICES = new String[] { "Action 1",
			"Action 2", "Action 3" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupview();
	}

	private void setupview()
	{
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1,
				ACTIVITY_CHOICES));
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id)
			{
                Toast.makeText(getApplicationContext(), ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
			}
		});

	}

}
