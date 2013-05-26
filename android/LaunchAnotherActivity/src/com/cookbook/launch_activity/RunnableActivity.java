package com.cookbook.launch_activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RunnableActivity extends Activity implements Runnable
{
    private int numberOfPressed = 0;
    private int xi;
    private int yi;
	private Thread t = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.run);
        setupView();
	}

	private void setupView()
	{
        final TextView tv = (TextView)findViewById(R.id.text);
        /* detectEdges(); */
        if( null == t) {
            t = new Thread(RunnableActivity.this);
        }
        t.start();
        
        Button stopButton = (Button)findViewById(R.id.stop_thread);
        stopButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                if(null != t){
                    Thread dummy = t;
                    t = null;
                    dummy.interrupt();	
                }
                finish(); 
			}
		});
        
        Button viewButton = (Button)findViewById(R.id.view_thread);
        viewButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                tv.setText("Pressed button " + ++numberOfPressed
                        + "times\n And Computation loop at "
                        + "(" + xi + ", " + yi +") pixels" 
                		);
			}
		});
	}

	@Override
	public void run()
	{
        System.out.println("detectEdges");
        detectEdges();//dummy function
        try {
			Thread.sleep(100000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * dummy function
	 */
	private void detectEdges()
	{
        int x_pixels = 4000;
        int y_pixels = 4000;
        double image_transform = 0;
        for (int xi = 0; xi < x_pixels; xi++) {
			for (int yi = 0; yi < y_pixels; yi++) {
                image_transform = Math.cosh(xi*yi/x_pixels/y_pixels);
			}
		}
		
	}

}
