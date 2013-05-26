package com.cookbook.layout_widgets;

import java.util.Random;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cookbook.launch_activity.R;
import com.cookbook.layout_widgets.BDI_Service.LocalBinder;
import com.navchina.bdi.event.BDI_Event;

public class BDI_Activity extends Activity
{
	private static final String TAG = "bdi_activity";
	BDI_Service bdiService;
	private TextView tv;
	private Button poll;
	protected boolean mBound;
	private Random rand = new Random();
	private boolean mBRunning = true;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (null != msg.obj) {
				int r = rand.nextInt(256);
				int g = rand.nextInt(256);
				int b = rand.nextInt(256);
				BDI_Event e = (BDI_Event) msg.obj;
				tv.setText(e.toString());
				tv.setTextColor(Color.rgb(r, g, b));
				tv.setBackgroundColor(Color.rgb(255-r, 255-g, 255-b));
			}
		}
	};
	
	private Thread updateUI = new Thread(new Runnable()
	{

		@Override
		public void run()
		{
			while (mBRunning) {
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				BDI_Event e = null;
				e = bdiService.getLastRTData();
				if (null != e) {
					handler.sendMessage(handler.obtainMessage(0, e));
				}
			}
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_bdi);
		setupView();

		Intent intent = new Intent(this, BDI_Service.class);
		startService(intent);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}
	

	@Override
	protected void onStart()
	{
		Log.i("bdi_activity", "onStart()");
		super.onStart();
		//updateUI.start();
		mBRunning = true;
	}
	
	@Override
	protected void onStop()
	{
		Log.i("bdi_activity", "onStop()");
		super.onStop();
		mBRunning = false;
	}

	@Override
	protected void onDestroy()
	{
		bdiService.testCall();
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
		bdiService.testCall();
		if (mBound) {
			unbindService(conn);
			mBound = false;
		}
	}

	private void setupView()
	{
		tv = (TextView) findViewById(R.id.bdi_text);
		tv.setText("Press poll to see the Last RTData");
		poll = (Button) findViewById(R.id.bdi_poll_rtdata);
		poll.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				tv.setText("Clear");
			}
		});
	}

	private ServiceConnection conn = new ServiceConnection()
	{

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			mBound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			LocalBinder binder = (LocalBinder) service;
			bdiService = binder.getService();
			mBound = true;
		}
	};

}
