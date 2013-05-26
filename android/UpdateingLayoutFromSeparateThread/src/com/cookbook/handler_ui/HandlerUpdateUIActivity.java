package com.cookbook.handler_ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HandlerUpdateUIActivity extends Activity
{
	private TextView av;
	private int text_string = R.string.start;
	private int background_color = Color.DKGRAY;

	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable()
	{
		@Override
		public void run()
		{
			av.setText(text_string);
			av.setBackgroundColor(background_color);
		}
	};
	private final static int SIZE = 100;
	double tmp;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setupView();
	}

	private void setupView()
	{
		av = (TextView) findViewById(R.id.computation_status);

		Button actionButton = (Button) findViewById(R.id.action);
		actionButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onClickAction();
			}
		});
	}

	protected void onClickAction()
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				text_string = R.string.start;
				background_color = Color.DKGRAY;
				mHandler.post(mUpdateResults);

				computation(1);
				text_string = R.string.first;
				background_color = Color.BLUE;
				mHandler.post(mUpdateResults);
                
				computation(2);
				text_string = R.string.second;
				background_color = Color.GREEN;
				mHandler.post(mUpdateResults);
			}
		});
		thread.start();
	}

	protected void computation(int val)
	{
		for (int ii = 0; ii < SIZE; ii++) {
			for (int jj = 0; jj < SIZE; jj++) {
                tmp = val*Math.log(ii+1)/Math.log1p(jj+1);
			}
		}
	}

}