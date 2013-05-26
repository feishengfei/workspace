package com.cookbook.layout_widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cookbook.launch_activity.R;

public class AlertDialogActivity extends Activity
{

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.showalert);
        setupView();
    }

	private void setupView()
	{
        Button showAlert = (Button)findViewById(R.id.show_alertdialog);
        showAlert.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                new AlertDialog.Builder(AlertDialogActivity.this)
                .setTitle("消息列子")
                .setMessage("我们来做个吐司吧")
                .setNeutralButton("这里这里", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
                        Toast.makeText(AlertDialogActivity.this, "点点看", 
                        		Toast.LENGTH_SHORT)
                		.show();						
					}
				})
                .show();
			}
		});
		
	}
}
