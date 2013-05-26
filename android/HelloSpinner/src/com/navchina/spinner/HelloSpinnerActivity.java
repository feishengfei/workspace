package com.navchina.spinner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HelloSpinnerActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		setupViews();
	}

	private void setupViews()
	{
		Spinner spinner = (Spinner) findViewById(R.id.planet_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.planets_array,
				android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener
	{

		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
				long arg3)
		{
			Toast.makeText(parent.getContext(),
					"您选择的是" + parent.getItemAtPosition(pos).toString(),
					Toast.LENGTH_LONG).show();
			TextView tv = (TextView)findViewById(R.id.spinner_result);
			tv.setText(parent.getItemAtPosition(pos).toString());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
		{
		}

	}
}