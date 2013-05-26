package com.cookbook.layout_widgets.list;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cookbook.launch_activity.R;

public class List2Activity extends ListActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setupView();
    }

	private void setupView()
	{
       setListAdapter(new ArrayAdapter<String>(this, 
    		   android.R.layout.simple_list_item_1, 
    		   getResources().getStringArray(R.array.weekday)));
	}
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
        Toast.makeText(this, 
        		getResources().getStringArray(R.array.weekday)[position], 
        		Toast.LENGTH_SHORT)
		.show();
		super.onListItemClick(l, v, position, id);
	}
}
