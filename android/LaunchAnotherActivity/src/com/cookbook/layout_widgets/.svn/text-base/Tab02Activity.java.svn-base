package com.cookbook.layout_widgets;

import com.cookbook.launch_activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.TabHost;

public class Tab02Activity extends Activity
{
    private TabHost tabs;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.tab02);
        setupView();
    }

	private void setupView()
	{
        tabs = (TabHost)findViewById(R.id.tabhost02);
        tabs.setup();
        
        TabHost.TabSpec spec = tabs.newTabSpec("button tab");
        
        spec.setContent(R.id.tab02_addtab);
        spec.setIndicator("Button Add");
        tabs.addTab(spec);
        
        Button b = (Button)findViewById(R.id.tab02_addtab);
        b.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                TabHost.TabSpec spec = tabs.newTabSpec("tag");
                
                spec.setContent(new TabHost.TabContentFactory()
				{
					
					@Override
					public View createTabContent(String tag)
					{
						return (new AnalogClock(Tab02Activity.this));
					}
				});
                spec.setIndicator("Clock");
                tabs.addTab(spec);
			}
		});
	}
}
