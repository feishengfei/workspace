package com.cookbook.launch_activity;

import com.cookbook.launch_service.SelfContainedService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuScreenActivity extends Activity implements Runnable
{
	private static final int PLAY_GAME = 1010;
	private TextView tv;
	private int meaningOfLife = 42;
	private String userName = "xzhou";
    static double[][] correlation;
    private static final int NUM_SAMPS = 1000;
    private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
            setContentView(R.layout.chap1_4);
       		setupView();
		}
    };
	private int mScore = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
       /* 
		setContentView(R.layout.loading);
        
		Thread thread = new Thread(this);
        thread.start();
        */
		setContentView(R.layout.chap1_4);
		setupView();
	}

	private void setupView()
	{
		tv = (TextView) findViewById(R.id.startscreen_text);
		tv.setText(userName + " : " + meaningOfLife);
        
		Button threadButton = (Button)findViewById(R.id.thread_demo);
        threadButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
                showThreadDemo();				
			}
		});
        
		Button playButton = (Button)findViewById(R.id.show_music);
        playButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                showMusic();
			}
		});

		Button startButton = (Button) findViewById(R.id.start_game);
		startButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startGame();
			}
		});
        
		Button showListButton = (Button) findViewById(R.id.show_list);
		showListButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showList();
			}
		});
        
		Button showCountDown = (Button)findViewById(R.id.show_countdown);
        showCountDown.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
			    showCountDown();	
			}
		});
        
        Button serviceStartButton = (Button)findViewById(R.id.service_start);
        serviceStartButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                startService(new Intent(MenuScreenActivity.this, 
                		SelfContainedService.class));
			}
		});
        
        Button serviceStopButton = (Button)findViewById(R.id.service_stop);
        serviceStopButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                stopService(new Intent(MenuScreenActivity.this,
                		SelfContainedService.class));
			}
		});
        
        Button showAlertDialogButton = (Button)findViewById(R.id.show_alertdialog);
        showAlertDialogButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                showAlertDialog();
			}
		});
        
        Button showNotificationButton = (Button)findViewById(R.id.show_notification);
        showNotificationButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                Context context = getApplicationContext();
                Intent i = new Intent(context, ShowNotificationActivity.class);
                startActivity(i);
			}
		});
        
        Button showPLayoutButton = (Button)findViewById(R.id.show_p_layout);
        showPLayoutButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                showPLayout();				
			}
		});
        
        Button showChangedTextButton = (Button)findViewById(R.id.show_changed_text);
        showChangedTextButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                showChangedText();				
			}
		});
        
        Button showTextEntryButton1 = (Button)findViewById(R.id.show_text_entry1);
        showTextEntryButton1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                showTextEntry1();
			}
		});
        
        Button showTextEntryButton2 = (Button)findViewById(R.id.show_text_entry2);
        showTextEntryButton2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                showTextEntry2();
			}
		});
        
	}

	protected void showTextEntry2()
	{
        Intent i = new Intent(this, TextEntryActivity2.class);
        startActivity(i);
	}

	protected void showTextEntry1()
	{
        Intent i = new Intent(this, TextEntryActivity1.class);
        startActivity(i);
	}

	protected void showChangedText()
	{
        Intent i = new Intent(this, ChangeFontActivity.class);
        startActivity(i);
	}

	protected void showPLayout()
	{
        Intent i = new Intent(this, ProgrammaticLayoutActivity.class);
        startActivity(i);
	}

	protected void showAlertDialog()
	{
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage("score:" + mScore +"");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "++", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				++mScore;
			}
		});
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "=0", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				mScore = 0;
			}
		});
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "--", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				--mScore;
			}
		});
        dialog.show();
	}

	protected void showCountDown()
	{
        Intent i = new Intent(this, CountDownActivity.class);
        startActivity(i);
	}

	protected void showThreadDemo()
	{
        Intent i = new Intent(this, RunnableActivity.class);
        startActivity(i);
	}

	protected void showMusic()
	{
		Intent i = new Intent(this, PlayMusicActivity.class);
		startActivity(i);
	}

	protected void showList()
	{
		Intent showList = new Intent(this, DemoListActivity.class);
		startActivity(showList);

	}

	protected void startGame()
	{
        //在外部实现
		Intent launchGame = new Intent(this, PlayGameActivity.class);
		launchGame.putExtra("meaningOfLife", meaningOfLife);
		launchGame.putExtra("userName", userName);
		startActivityForResult(launchGame, PLAY_GAME);
	}

	@Override
	protected void onActivityResult(int requestCode, 
			int resultCode, Intent data)
	{
        System.out.println("requestCode:" + requestCode);
        System.out.println("resultCode:" + resultCode);
        if (requestCode == PLAY_GAME && resultCode == RESULT_OK) {
             meaningOfLife = data.getExtras().getInt("returnInt");
             userName = data.getExtras().getString("userName");
             tv.setText(userName + " : " + meaningOfLife);
        }
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void run()
	{
        initializeArrays();		
        mHandler.sendEmptyMessage(0);
	}
    
	private void initializeArrays()
	{
        if(correlation!=null)return;
        
        correlation = new double[NUM_SAMPS][NUM_SAMPS];
        for (int k = 0; k < NUM_SAMPS; k++) {
			for (int m = 0; m < NUM_SAMPS; m++) {
				correlation[k][m] = Math.cos(2*Math.PI*(k+m)/1000);
			}
		}
	}
    
	
}