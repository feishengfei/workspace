package com.cookbook.launch_service;

import com.cookbook.launch_activity.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class SelfContainedService extends Service
{
	private static final int[] NOTES = {
		R.raw.c5,
		R.raw.b4,
		R.raw.a4,
		R.raw.g4,
	};			
	private MediaPlayer m_mediaPlayer;
    private boolean paused = false;
    private static final int NOTE_DURATION = 400;
        
    
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
        Toast.makeText(this, "Service created...", 
        		Toast.LENGTH_SHORT).show();
        System.out.println("Service created...");
        paused = false;
        Thread initBkThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
                play_music();
			}
		});
        initBkThread.start();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
        Toast.makeText(this, "Service destroyed...", Toast.LENGTH_SHORT).show();
        paused = true;
        System.out.println("Service destroyed...");
	}
    
	private void play_music() 
	{
        for (int ii = 0; ii < NOTES.length*3; ii++) {
			if(!paused) {
			    if(m_mediaPlayer != null)	{
			    	m_mediaPlayer.release();
			    }
                m_mediaPlayer = MediaPlayer.create(this, NOTES[ii%4]);
                m_mediaPlayer.start();
                try {
					Thread.sleep(NOTE_DURATION);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
