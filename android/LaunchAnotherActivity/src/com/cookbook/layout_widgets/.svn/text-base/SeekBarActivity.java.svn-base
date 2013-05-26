package com.cookbook.layout_widgets;

import com.cookbook.launch_activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarActivity extends Activity
{
    private SeekBar m_seekBar;
	protected int count;
	private TextView seekValue;
    private String prefix;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.seekbar);
        setupView();
    }

	private void setupView()
	{
        seekValue = (TextView)findViewById(R.id.seek_value);
        m_seekBar = (SeekBar)findViewById(R.id.seekbar01);
        m_seekBar.setMax(100);
        m_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			    //seekValue.setText(seekBar.getProgress())	;
			    seekValue.setText("stop")	;
                System.out.println("stop");
                prefix = "stop";
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
                
			   // seekValue.setText(seekBar.getProgress())	;
			    seekValue.setText("start")	;
                System.out.println("start");
                prefix = "start";
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser)
			{
                if(fromUser) count = progress;
                System.out.println(progress);
                seekValue.setText(prefix + progress);
			}
		});
        
        /*
        Thread initThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
                show_time();
			}
		});
        */
		
	}

	protected void show_time()
	{
        for(count = 0; count < 100; count++) {
            m_seekBar.setProgress(count);
            
            try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}
    
}
