package com.cookbook.layout_widgets;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cookbook.launch_activity.R;

public class ToggleButtonActivity extends Activity
{
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.togglebutton);
		setupView();
	}

	private void setupView()
	{
        tv = (TextView)findViewById(R.id.status);
        
        class Toppings {
        	private boolean LETTUCE, TOMATO, CHEESE;
            @Override
            public String toString()
            {
            	return "lecttuce:" + String.format("%s\n", LETTUCE?"yes":"no") 
            	 + "tomato:" + String.format("%s\n", TOMATO?"yes":"no")
            	 + "cheese:" + String.format("%s\n", CHEESE?"yes":"no")
            	;
            }
        }
        
        final Toppings sandwichToppings = new Toppings();
        final ToggleButton togglebutton[] = {
            (ToggleButton)findViewById(R.id.togglebutton0),
            (ToggleButton)findViewById(R.id.togglebutton1),
            (ToggleButton)findViewById(R.id.togglebutton2),
        };
        togglebutton[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
                sandwichToppings.LETTUCE = isChecked;				
                tv.setText(sandwichToppings.toString());
			}
		});
        togglebutton[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
                sandwichToppings.TOMATO = isChecked;				
                tv.setText(sandwichToppings.toString());
			}
		});
        togglebutton[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
                sandwichToppings.CHEESE = isChecked;				
                tv.setText(sandwichToppings.toString());
			}
		});

	}
}
