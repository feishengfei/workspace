package com.cookbook.layout_widgets;

import java.util.concurrent.LinkedBlockingQueue;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.navchina.bdi.BDI_Daemon;
import com.navchina.bdi.event.BDI_Event;

public class BDI_Service extends Service
{
	private static final String TAG = "BDI_service";
	private final IBinder mBinder = new LocalBinder();
	private BDI_Daemon d = null;
	private boolean bRunning = false;

	@Override
	public IBinder onBind(Intent intent)
	{
		Log.i("BDI_service", "onBind");
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		Log.i("BDI_service", "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent)
	{
		Log.i("BDI_service", "onRebind");
		super.onRebind(intent);
	}

	@Override
	public void onCreate()
	{
		Log.i("BDI_service", "onCreate");
		super.onCreate();
		startDaemon();

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				bRunning = true;
				while (bRunning) {
					BDI_Event e = null;
					e = d.getRTData();
					if (e != null) {
						Log.i(TAG, "broadCast intent");
						Intent i = new Intent();
						i.putExtra("e", e.toString());
						i.setAction("com.navchina.xzhou.action_test");
						BDI_Service.this.sendBroadcast(i);
					}
				}

			}
		}).start();
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		bRunning = false;
	}

	private void startDaemon()
	{
		Log.i("BDI_service", "startDaemon()");
		if (null == d) {
			d = new BDI_Daemon();
			d.setUsr("98000001");
			d.setPwd("123456");
			new Thread(d).start();
		}
	}

	public class LocalBinder extends Binder
	{
		BDI_Service getService()
		{
			return BDI_Service.this;
		}
	}

	public BDI_Event getLastRTData()
	{
		BDI_Event res = null;
		res = d.getRTData();
		Log.i("BDI_service", "getLastRTData()");
		return res;
	}
	
	public void testCall()
	{
		Log.i(TAG, "testCall");
	}

	public int getRTQueueSize()
	{
		int ret = 0;
		LinkedBlockingQueue<BDI_Event> qe = (LinkedBlockingQueue<BDI_Event>) d
				.getRtdq();
		ret = qe.size();
		return ret;
	}

}
