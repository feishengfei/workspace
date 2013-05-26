package com.cookbook.launch_activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountDownActivity extends Activity
{

	private TextView mTimeLabel;
	private TextView mButtonLabel;
	private int buttonPress = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown);
        setupView();
	}

	private void setupView()
	{
        mTimeLabel = (TextView)findViewById(R.id.text);
        mButtonLabel = (TextView)findViewById(R.id.trigger);
        
        new CountDownTimer(30000, 1000)
		{
			
			@Override
			public void onTick(long millisUntilFinished)
			{
                mTimeLabel.setText("seconds remaining: "
                		+ millisUntilFinished/1000);
			}
			
			@Override
			public void onFinish()
			{
                mTimeLabel.setText("done!");
			}
		}.start();
        
		Button startButton = (Button)findViewById(R.id.trigger);
        startButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                mButtonLabel.setText("Pressed " + ++buttonPress + " times");
			}
		});
	}

}
