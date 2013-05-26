package com.cookbook.layout_widgets;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.launch_activity.R;
import com.cookbook.launch_service.ISimpleMathService;
import com.cookbook.launch_service.SimpleMathService;

public class SimpleMathServiceDemo extends Activity
{
	private EditText num1;
	private EditText num2;
	private Button add;
	private Button sub;
	private Button echo;
	private ISimpleMathService service = null;
	private boolean bound;

	private ServiceConnection conn = new ServiceConnection()
	{

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			service = null;
			Toast.makeText(SimpleMathServiceDemo.this,
					"disconnect from Service", Toast.LENGTH_SHORT).show();
			bound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			SimpleMathServiceDemo.this.service = ISimpleMathService.Stub
					.asInterface(service);
			Toast.makeText(SimpleMathServiceDemo.this, "connect to Service",
					Toast.LENGTH_SHORT).show();
			bound = true;
		}
	};
	private TextView resText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_math_demo);
		setupView();
	}

	private void setupView()
	{
		resText = (TextView) findViewById(R.id.simple_math_result);
		num1 = (EditText) findViewById(R.id.simple_math_num1);
		num2 = (EditText) findViewById(R.id.simple_math_num2);
		add = (Button) findViewById(R.id.simple_math_add);
		add.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					int ret = service.add(
							Integer.parseInt(num1.getText().toString()),
							Integer.parseInt(num2.getText().toString()));
					resText.setText(String.valueOf(ret));
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		sub = (Button) findViewById(R.id.simple_math_sub);
		sub.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					int ret = service.substract(
							Integer.parseInt(num1.getText().toString()),
							Integer.parseInt(num2.getText().toString()));
					resText.setText(String.valueOf(ret));
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		echo = (Button) findViewById(R.id.simple_math_echo);
		echo.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					String ret = service.echo(resText.getText().toString());
					resText.setText(ret);
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	protected void onStart()
	{
		super.onStart();
		if (!bound) {
			bindService(new Intent(SimpleMathServiceDemo.this,
					SimpleMathService.class), conn, Context.BIND_AUTO_CREATE);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (bound) {
			bound = false;
			this.unbindService(conn);
		}
	}
}
