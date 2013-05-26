package com.cookbook.layout_widgets;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cookbook.launch_activity.R;

public class ButtonReverseActivity extends Activity
{
    private TextView tv;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.buttonreverse);
        setupView();
    }

	private void setupView()
	{
        tv = (TextView)findViewById(R.id.br_text);
	}
    
	public void updateBrText(View theButton)
	{
	    tv.setText(new Date(null).toString());
	}
}
