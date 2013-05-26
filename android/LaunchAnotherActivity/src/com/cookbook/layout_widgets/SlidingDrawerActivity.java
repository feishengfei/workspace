package com.cookbook.layout_widgets;

import com.cookbook.launch_activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SlidingDrawer;
import android.widget.Toast;

public class SlidingDrawerActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidingdrawer01);
        setupView();
    }

	private void setupView()
    {
        SlidingDrawer sd =  (SlidingDrawer)findViewById(R.id.drawer01);
        sd.animateOpen();
        sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed()
			{
			    Toast.makeText(SlidingDrawerActivity.this, "芝麻关门", Toast.LENGTH_SHORT).show();
			}
		});
        sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
			
			@Override
			public void onDrawerOpened()
			{
			    Toast.makeText(SlidingDrawerActivity.this, "芝麻开门", Toast.LENGTH_SHORT).show();
			}
		});
	    
    }
}
