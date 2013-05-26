package com.cookbook.layout_widgets;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.cookbook.launch_activity.R;

public class Flipper01Activity extends Activity
{
    private ViewFlipper flipper;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.flipper01);
        setupView();
    }

	private void setupView()
    {
        flipper = (ViewFlipper)findViewById(R.id.flipper01_detail);
        Button flipBtn = (Button)findViewById(R.id.flip_me);
        flipBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
                flipper.showNext();
			}
		});
    }
     
}
