package com.cookbook.launch_activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayMusicActivity extends Activity
{
    private static final int[] NOTES = {
            R.raw.c5,
            R.raw.b4,
            R.raw.a4,
            R.raw.g4,
    };
    private static final int NOTE_DURATION = 400;
    private boolean paused = false;
	private MediaPlayer m_mediaPlayer;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.music);
        setupView();
	}

	private void setupView()
	{
        Button playButton = (Button)findViewById(R.id.play_music);
        playButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                Thread initBkgThread = new Thread(new Runnable()
				{
					
					@Override
					public void run()
					{
                        play_music();
					}
				});
                initBkgThread.start();
			}
		});
	}
    
	@Override
	protected void onPause()
	{
        paused = true;
		super.onPause();
	}

	@Override
	protected void onResume()
	{
        paused = false;
		super.onResume();
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
