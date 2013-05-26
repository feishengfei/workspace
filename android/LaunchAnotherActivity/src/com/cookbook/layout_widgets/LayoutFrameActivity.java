package com.cookbook.layout_widgets;

import java.util.Timer;
import java.util.TimerTask;

import com.cookbook.launch_activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class LayoutFrameActivity extends Activity
{
	private int currentColor = 0;
	final int[] colors = new int[] { R.color.r1, R.color.r2, R.color.r3,
			R.color.r4, R.color.r5, R.color.r6, R.color.r7, };

	final int[] names = new int[] { R.id.frame_view01, R.id.frame_view02,
			R.id.frame_view03, R.id.frame_view04, R.id.frame_view05,
			R.id.frame_view06, R.id.frame_view07, };

	TextView[] views = new TextView[7];

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_frame);
		for (int i = 0; i < views.length; i++) {
			views[i] = (TextView) findViewById(names[i]);
		}

		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				if (0x1122 == msg.what) {
					for (int i = 0; i < 7 - currentColor; i++) {
						views[i].setBackgroundResource(colors[i + currentColor]);
					}
					for (int i = 7 - currentColor, j = 0; i < 7; i++, j++) {
						views[i].setBackgroundResource(colors[j]);
					}
				}
				super.handleMessage(msg);
			}
		};

		new Timer().schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				currentColor++;
				if (currentColor >= 6) {
					currentColor = 0;
				}
				Message m = new Message();
				m.what = 0x1122;
				handler.sendMessage(m);

			}
		}, 0, 1000);

	}
}
