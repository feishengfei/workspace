package com.xmobileapp.hello;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Hello extends Activity {
    
	Button mButton;
	TextView mText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mText = (TextView) findViewById(R.id.text);
        mText.setVisibility(View.GONE);
        
        mButton = (Button) findViewById(R.id.button);        
        mButton.setOnClickListener(new ClickMeListener());        
    }
    
    private class ClickMeListener implements OnClickListener {
    	public void onClick(View v) {
    		mButton.setVisibility(View.GONE);
    		mText.setVisibility(View.VISIBLE);
    	}
    }
}