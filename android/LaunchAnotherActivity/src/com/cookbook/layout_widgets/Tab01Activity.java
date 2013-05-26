package com.cookbook.layout_widgets;

import com.cookbook.launch_activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class Tab01Activity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.tab01);
        setupView();
    }

	private void setupView()
	{
        TabHost tabs = (TabHost)findViewById(R.id.tabhost01);
        tabs.setup();
        
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab01_01);
        spec.setIndicator("clock");
        tabs.addTab(spec);
        
        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab01_02);
        spec.setIndicator("button");
        tabs.addTab(spec);
	}
}
