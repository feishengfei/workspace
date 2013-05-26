package com.cookbook.layouts;

import com.cookbook.launch_activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class LinearLayoutActivity extends Activity
{
    private RadioGroup orientation;
	private RadioGroup gravity;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.linearlayout);
        setupView();
    }

	private void setupView()
	{
        orientation = (RadioGroup)findViewById(R.id.orientation);
        orientation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
                switch (checkedId) {
					case R.id.rb_horizontal:
						orientation.setOrientation(LinearLayout.HORIZONTAL);
						break;
					case R.id.rb_vertical:
						orientation.setOrientation(LinearLayout.VERTICAL);
						break;
					default:
						break;
				}				
			}
		});
        gravity = (RadioGroup)findViewById(R.id.gravity);
        gravity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
			    switch (checkedId) {
					case R.id.rb_left:
						gravity.setGravity(Gravity.LEFT);
						break;
					case R.id.rb_center:
						gravity.setGravity(Gravity.CENTER);
						break;
					case R.id.rb_right:
						gravity.setGravity(Gravity.RIGHT);
						break;
					default:
						break;
				}	
			}
		});
	}
}
