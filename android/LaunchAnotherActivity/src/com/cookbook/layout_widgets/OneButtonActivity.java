package com.cookbook.layout_widgets;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OneButtonActivity extends Activity implements OnClickListener
{
    private Button btn;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        btn = new Button(this);
        btn.setOnClickListener(this);
        updateTime();
        setContentView(btn);
    }
    
	@Override
	public void onClick(View v)
	{
        updateTime();		
	}

	private void updateTime()
	{
        btn.setText(new Date().toString());
	}
}
