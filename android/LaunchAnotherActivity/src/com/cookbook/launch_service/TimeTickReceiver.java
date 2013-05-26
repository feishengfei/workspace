package com.cookbook.launch_service;

import android.app.LauncherActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TimeTickReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Toast.makeText(context, "Welcome to xzhou_galaxy p7500", Toast.LENGTH_SHORT).show();
	}

}
