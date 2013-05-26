package com.cookbook.layout_widgets;

import com.cookbook.launch_activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerActivity extends Activity
{
    private static final String[]  OCEANS = {
        "Pacific",
        "Atlantic",
        "Indian",
        "Arctic",
        "Southern",
    };
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner);
        setupView();
    }

	private void setupView()
	{
        Spinner favOcean = (Spinner)findViewById(R.id.spinner);
        
        ArrayAdapter<String> mAdapter = new 
            ArrayAdapter<String>(this, R.layout.spinner_entry, OCEANS);
        mAdapter.setDropDownViewResource(R.layout.spinner_entry);
        favOcean.setAdapter(mAdapter);
	}
}
