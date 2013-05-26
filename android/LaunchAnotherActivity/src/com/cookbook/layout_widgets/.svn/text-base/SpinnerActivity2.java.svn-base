package com.cookbook.layout_widgets;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cookbook.launch_activity.R;

public class SpinnerActivity2 extends Activity
{
	private TextView selection;
    private static final String[] items = {
        "Mon",
        "Tue",
        "Wed",
        "Thir",
        "Friday",
        "",
        "Sat",
        "Sun",
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner2);
        setupView();
    }

	private void setupView()
	{
        selection = (TextView)findViewById(R.id.selection2);
        
        Spinner spin = (Spinner)findViewById(R.id.spinner2);
        spin.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
			    selection.setText(items[arg2]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
    			selection.setText("");
			}
		});
	
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, 
        		android.R.layout.simple_spinner_item, items);
        spin.setAdapter(aa);
	}
}
