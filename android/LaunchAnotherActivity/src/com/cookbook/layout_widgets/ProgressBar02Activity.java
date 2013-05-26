package com.cookbook.layout_widgets;

import java.util.concurrent.atomic.AtomicBoolean;

import com.cookbook.launch_activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class ProgressBar02Activity extends Activity
{
    ProgressBar bar;
    Handler handler = new Handler() {
    	@Override
        public void handleMessage(android.os.Message msg) {
            bar.incrementProgressBy(1);
        };	
    };
    AtomicBoolean isRunning = new AtomicBoolean(false);
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.progressbar02);
        bar = (ProgressBar)findViewById(R.id.progress_bar_02);
    }
    
    @Override
    protected void onStart()
    {
    	super.onStart();
        bar.setProgress(0);
        
        Thread background = new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
			    try {
					for (int i=0; i<100 && isRunning.get(); i++) {
					    Thread.sleep(100);
                        handler.sendMessage(handler.obtainMessage(1));
					}
				}
				catch (Throwable t) {
				}	
			}
		});
        
        isRunning.set(true);
        background.start();
    }
    
    @Override
    protected void onStop()
    {
    	super.onStop();
        isRunning.set(false);
    }
}
