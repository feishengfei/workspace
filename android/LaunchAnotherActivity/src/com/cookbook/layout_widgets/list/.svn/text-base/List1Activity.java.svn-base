package com.cookbook.layout_widgets.list;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cookbook.launch_activity.R;

public class List1Activity extends ListActivity 
{
    private static final String[] items = {
        "lorem", "ipsum", "dolor",
        "sit", "amet",
        "consectetuer", "adipiscing", "elit", "morbi", "vel",
        "ligula", "vitae", "arcu", "aliquet", "mollis",
        "etiam", "vel", "erat", "placerat", "ante",
        "porttitor", "sodales", "pellentesque", "augue", "purus"
    };
	private TextView selection; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.list1);
        setupView();
    }

	private void setupView()
	{
        setListAdapter(new ArrayAdapter<String>(this, 
        		android.R.layout.simple_list_item_1, items));
        selection = (TextView)findViewById(R.id.list1_text);
	}
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
        selection.setText(items[position]);
	}
}
