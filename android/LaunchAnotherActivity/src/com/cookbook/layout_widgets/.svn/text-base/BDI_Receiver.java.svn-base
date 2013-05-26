package com.cookbook.layout_widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BDI_Receiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		String eStr = intent.getExtras().getString("e");
		Toast.makeText(context, intent.getAction() + "\n" + eStr, Toast.LENGTH_LONG).show();
		Log.i("BDI_Receiver", intent.getAction() + "\n" + eStr);
	}
}
