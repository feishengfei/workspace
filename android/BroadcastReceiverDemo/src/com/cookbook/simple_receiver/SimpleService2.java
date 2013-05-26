package com.cookbook.simple_receiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class SimpleService2 extends Service
{
    
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
        Toast.makeText(this, 
        		"Service created ...", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
        Toast.makeText(this, 
        		"Service destroyed ...", Toast.LENGTH_SHORT).show();
	}
    
	

}
