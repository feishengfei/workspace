package com.cookbook.launch_activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.widget.Button;

public class RotationActivity extends Activity
{
    private static final int PICK_REQUEST = 1234;
	private Button viewButton;
    Uri contact = null;
	private Button pickButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.twobutton);
        setupView(savedInstanceState);
    }

	private void setupView(Bundle savedInstanceState)
	{
        viewButton = (Button)findViewById(R.id.two_btn_view);
        viewButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                startActivity(new Intent(Intent.ACTION_VIEW, 
                		contact));
			}
		});
        
        //
        restoreMe(savedInstanceState);
        
        pickButton = (Button)findViewById(R.id.two_btn_pick);
        pickButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			    Intent i = new Intent(Intent.ACTION_PICK
			    		 , Contacts.CONTENT_URI)	;
                startActivityForResult(i, PICK_REQUEST);
			}
		});
        viewButton.setEnabled(contact!=null);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        if (requestCode==PICK_REQUEST && 
        		resultCode==RESULT_OK) {
        	contact=data.getData();
            viewButton.setEnabled(true);
        }
	}

	private void restoreMe(Bundle savedInstanceState)
	{
        contact = null;		
        if (savedInstanceState != null){
            String contactUri = 
            	savedInstanceState.getString("contact")	;
            if (contactUri != null) {
                contact=Uri.parse(contactUri);
            }
        }
	}
    
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
        if (null != contact) {
        	outState.putString("contact", contact.toString());
        }
	}
}
