package com.cookbook.layout_widgets;

import com.cookbook.launch_activity.R;
import com.cookbook.launch_activity.R.id;
import com.cookbook.launch_activity.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class ProgressBarActivity extends Activity
{
	private static ProgressBar m_progressBar;
	int percent_done = 0;

	final Handler mHandler = new Handler();

	final Runnable mUpdateResults = new Runnable()
	{

		@Override
		public void run()
		{
			m_progressBar.setProgress(percent_done);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressbar);
		setupView();
	}

	private void setupView()
	{
		m_progressBar = (ProgressBar) findViewById(R.id.ex_progress_bar);
		Button actionButton = (Button) findViewById(R.id.start_progress);
		actionButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				do_work();
			}
		});

	}

	protected void do_work()
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				percent_done = 0;
				mHandler.post(mUpdateResults);

				computation(1);
				percent_done = 50;
				mHandler.post(mUpdateResults);

				computation(2);
				percent_done = 100;
				mHandler.post(mUpdateResults);
			}
		});
		thread.start();
	}

	private static final int SIZE = 100;
	double tmp;

	protected void computation(int val)
	{
		for (int ii = 0; ii < SIZE; ii++) {
			for (int jj = 0; jj < SIZE; jj++) {
                tmp = val*Math.log(ii+1)/Math.log1p(jj+1);
			}
		}

	}

}
