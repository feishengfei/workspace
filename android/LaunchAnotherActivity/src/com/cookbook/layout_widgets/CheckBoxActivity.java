package com.cookbook.layout_widgets;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cookbook.launch_activity.R;

public class CheckBoxActivity extends Activity
{
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ckbox);
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
		final CheckBox checkbox[] = { 
			(CheckBox) findViewById(R.id.checkbox0), 
			(CheckBox) findViewById(R.id.checkbox1), 
			(CheckBox) findViewById(R.id.checkbox2), 
		};
        checkbox[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
                sandwichToppings.LETTUCE = isChecked;				
                tv.setText(sandwichToppings.toString());
			}
		});
        checkbox[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
                sandwichToppings.TOMATO = isChecked;				
                tv.setText(sandwichToppings.toString());
			}
		});
        checkbox[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
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
