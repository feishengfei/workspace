package com.cookbook.launch_activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TextEntryActivity1 extends Activity
{
	private EditText phoneNumberET;
	protected CharSequence phoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.textentry1);
        setupView();
	}

	private void setupView()
	{
        phoneNumberET = (EditText)findViewById(R.id.phone_number);
        phoneNumberET.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
                if((event.getAction() == KeyEvent.ACTION_DOWN) &&
                		(keyCode == KeyEvent.KEYCODE_ENTER)){
                    phoneNumber = phoneNumberET.getText();
                    Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT);
                    System.out.println(phoneNumber);
                    return true	;
                }
				return false;
			}
		});
        
	}

}
