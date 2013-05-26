package com.cookbook.simple_activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class SimpleActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toast.makeText(this, "oncreate(Bundle savedInstanceState)", Toast.LENGTH_SHORT).show();
    }

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
	}
    
	@Override
	protected void onStop()
	{
		super.onStop();
        Toast.makeText(this, "onStop()", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
        Toast.makeText(this, "onPause()", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
        Toast.makeText(this, "onRestart()", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
        Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
	}
}