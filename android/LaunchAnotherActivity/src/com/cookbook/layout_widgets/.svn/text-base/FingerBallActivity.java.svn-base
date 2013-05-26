package com.cookbook.layout_widgets;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.cookbook.launch_activity.R;
import com.cookbook.view.DrawView;

public class FingerBallActivity extends Activity
{


	private DrawView draw;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingerball);
		
		LinearLayout root = (LinearLayout)findViewById(R.id.finger_ball_root);
		
		draw = new DrawView(this);
		draw.setMinimumWidth(300);
		draw.setMinimumHeight(500);
		draw.setOnTouchListener(onTouch);
		root.addView(draw);
	}
	
	private OnTouchListener onTouch = new View.OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			draw.setX(event.getX());
			draw.setY(event.getY());
			draw.invalidate();
			return false;
		}
	};
	
}
