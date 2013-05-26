package com.cookbook.layout_widgets;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.cookbook.launch_activity.R;

public class Flipper02Activity extends Activity
{
    private static final String[] items = {
        "Are",
        "you",
        "ready?",
    };
    private ViewFlipper flipper;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.flipper02);
        setupView();
    }

	private void setupView()
    {
        flipper = (ViewFlipper)findViewById(R.id.flipper02_detail);
        for (String item : items) {
            Button btn = new Button(this);
            btn.setText(item);
            flipper.addView(btn, 
            		new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 
            				ViewGroup.LayoutParams.FILL_PARENT));
        }
        flipper.setFlipInterval(2000);
        flipper.startFlipping();
    }
     
}
