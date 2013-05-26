package com.cookbook.launch_activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PickPlaceActivity extends Activity
{
    private EditText lat;
	private EditText lon;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.pickplace);
        setupView();
    }

	private void setupView()
	{
        lat = (EditText)findViewById(R.id.pickplace_lat);
        lon = (EditText)findViewById(R.id.pickplace_lon);
        
        Button showPlace = (Button)findViewById(R.id.pickplace_2_map);
        showPlace.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			    String _lat = lat.getText().toString();	
			    String _lon = lon.getText().toString();	
                /*
                Uri uri = Uri.parse("geo:" + _lat + "," + _lon);
                
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                */
                Intent i = getIntent();
                i.putExtra("lat", _lat);
                i.putExtra("lon", _lon);
                setResult(RESULT_OK, i);
                finish();
			}
		});
		
	}
}
