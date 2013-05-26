package com.navchina.bdi.thread;

import java.util.concurrent.atomic.AtomicBoolean;

import com.navchina.bdi.BDI_Daemon;

public class BDI_Thread extends Thread 
{
	public static final int RT_PEER = 1;
	public static final int RTEX_PEER = 2;
	public static final int RR_PEER = 3;
	
	protected BDI_Daemon d;
	
	protected AtomicBoolean bRunning = new AtomicBoolean(false);
	protected AtomicBoolean bFinished = new AtomicBoolean(true);
	
	public BDI_Thread(BDI_Daemon d)
	{
		this.d = d;
	}
	
	boolean running() {
		return bRunning.get();
	}
	
	boolean finished() {
		return bFinished.get();
	}
	
	boolean busy() {
		return bFinished.get();
	}
	
	@Override
	public synchronized void start()
	{
		super.start();
		bRunning.set(true);
		bFinished.set(false);
	}
}