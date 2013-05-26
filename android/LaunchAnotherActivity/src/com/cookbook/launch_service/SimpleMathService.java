package com.cookbook.launch_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class SimpleMathService extends Service
{
	private final ISimpleMathService.Stub binder = new ISimpleMathService.Stub()
	{

		@Override
		public int substract(int a, int b) throws RemoteException
		{
			return a - b;
		}

		@Override
		public String echo(String input) throws RemoteException
		{
			return "echo" + input;
		}

		@Override
		public int add(int a, int b) throws RemoteException
		{
			return a + b;
		}
	};

	@Override
	public IBinder onBind(Intent intent)
	{
		return binder;
	}
}
