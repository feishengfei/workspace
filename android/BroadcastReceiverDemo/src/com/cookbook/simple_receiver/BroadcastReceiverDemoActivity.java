package com.cookbook.simple_receiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class BroadcastReceiverDemoActivity extends Activity {
    private SimpleBroadcastReceiver intentReceiver = new SimpleBroadcastReceiver();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupView();        
    }

	private void setupView()
	{
        IntentFilter intentFilter =
        	new IntentFilter(Intent.ACTION_CALL_BUTTON);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        registerReceiver(intentReceiver, intentFilter);
	}

	@Override
	protected void onDestroy()
	{
        unregisterReceiver(intentReceiver);
		super.onDestroy();
	}
    
	
}