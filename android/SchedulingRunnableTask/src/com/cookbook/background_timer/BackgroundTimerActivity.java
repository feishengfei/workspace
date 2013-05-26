package com.cookbook.background_timer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BackgroundTimerActivity extends Activity {
    private long mStartTime;
    private Handler mHandler = new Handler();
	private TextView mTimeLabel;
	private TextView mButtonLabel;
	private int bunntonPress;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupView();
    }

	private void setupView()
	{
        if(mStartTime == 0L) {
            mStartTime = SystemClock.uptimeMillis();	
            mHandler.removeCallbacks(mUpdateTimeTask);
        }
        
        mTimeLabel = (TextView)findViewById(R.id.text);
        mButtonLabel = (TextView)findViewById(R.id.trigger);
        
        Button startButton = (Button)findViewById(R.id.trigger);
        startButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                mButtonLabel.setText("Pressed" + ++bunntonPress 
                		+ " times");
			}
		});
	}
    
	private Runnable mUpdateTimeTask = new Runnable()
	{
		@Override
		public void run()
		{
            final long start = mStartTime;			
            long millis = SystemClock.uptimeMillis() - start;
            int seconds = (int)(millis/1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            mTimeLabel.setText("" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            mHandler.postDelayed(this, 200);
            
		}
	};


	@Override
	protected void onPause()
	{
        mHandler.removeCallbacks(mUpdateTimeTask);
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
        mHandler.postDelayed(mUpdateTimeTask, 100);
	}
    
	
}