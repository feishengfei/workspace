package com.cookbook.launch_activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestIntentActivity extends Activity
{
	private static final int PICK_PLACE = 0x01;
	private TextView geo;
	private String lat = "39.99";
	private String lon = "116.56";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testintent);
		setupView();
	}

	private void setupView()
	{
		geo = (TextView) findViewById(R.id.testintent_geo);
		geo.setText(lat + "," + lon);
		Button btn01 = (Button) findViewById(R.id.testintent_btn01);
		btn01.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://cn.engadget.com"));
				startActivity(i);
			}
		});

		Button btn02 = (Button) findViewById(R.id.testintent_btn02);
		btn02.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel:15001399705"));
				startActivity(i);
			}
		});

		Button btn03 = (Button) findViewById(R.id.testintent_btn03);
		btn03.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Intent.ACTION_CALL, Uri
						.parse("tel:10086"));
				startActivity(i);
			}
		});

		Button btn04 = (Button) findViewById(R.id.testintent_btn04);
		btn04.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse("geo:"+lat+","+lon));
				startActivity(i);
			}
		});

		Button btn05 = (Button) findViewById(R.id.testintent_btn05);
		btn05.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(TestIntentActivity.this,
						PickPlaceActivity.class);
				startActivityForResult(i, PICK_PLACE);
			}
		});
        /*
		Button btn06 = (Button) findViewById(R.id.testintent_btn06);
		btn06.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(TestIntentActivity.this,
						PickPlaceActivity.class);
				startActivityForResult(i, PICK_PLACE);
			}
		});
        */
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		String text = "requestCode:" + requestCode + "\\n" + "resultCode:"
				+ resultCode;
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

		if (requestCode == PICK_PLACE && resultCode == RESULT_OK) {
			lat = data.getExtras().getString("lat");
			lon = data.getExtras().getString("lon");
			geo.setText(lon + "," + lat);
			Toast.makeText(this, lon + "," + lat, Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
