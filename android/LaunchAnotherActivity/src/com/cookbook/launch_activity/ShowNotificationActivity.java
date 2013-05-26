package com.cookbook.launch_activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShowNotificationActivity extends Activity
{
	private NotificationManager mNManager;
	private static final int NOTIFY_ID = 1100;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        setupView();
	}
    
	private void setupView()
	{
        String ns = Context.NOTIFICATION_SERVICE;
        mNManager = (NotificationManager)getSystemService(ns);
	    final Notification msg = new Notification(R.drawable.ic_launcher, 
	    		"New event of importance",
	    		System.currentTimeMillis());
        
	    Button notiStartButton = (Button)findViewById(R.id.noti_start);
        Button notiCancelButton = (Button)findViewById(R.id.noti_cancel);
        
        notiStartButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			    Context context = getApplicationContext();	
                CharSequence contentTitle = "ShowNotification Example";
                CharSequence contentText = "Browse Android Cookbook Site";
                Intent msgIntent = new Intent(Intent.ACTION_VIEW, 
                		Uri.parse("http://renren.com"));
                PendingIntent intent = 
                    PendingIntent.getActivity(ShowNotificationActivity.this, 
                    		0, msgIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
                
                msg.defaults |= Notification.DEFAULT_SOUND;
                msg.flags |= Notification.FLAG_AUTO_CANCEL;
                
                msg.setLatestEventInfo(context, contentTitle, contentText, intent);
                mNManager.notify(NOTIFY_ID, msg);
			}
		});
        
        notiCancelButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			    mNManager.cancel(NOTIFY_ID);
			}
		});
	}
    
    	
}
