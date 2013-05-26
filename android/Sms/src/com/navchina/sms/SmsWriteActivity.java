package com.navchina.sms;

import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.navchina.R;
import com.navchina.sms.model.BDMsg;
import com.navchina.sms.model.MsgSQLiteOpenHelper;

public class SmsWriteActivity extends Activity
{
	private AlertDialog saveDraftDialog;
	private EditText contentTV;
	private AutoCompleteTextView numTV;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupView();
	}

	private void setupView()
	{
		setContentView(R.layout.sms_write);
		Intent intent = getIntent();
		String num = intent.getStringExtra("num");
		String content = intent.getStringExtra("content");

		numTV = (AutoCompleteTextView) findViewById(R.id.sms_write_number);
		numTV.setText(num);

		contentTV = (EditText) findViewById(R.id.sms_write_content);
		contentTV.setText(content);

		Button sendBtn = (Button) findViewById(R.id.sms_write_btn_send);
		sendBtn.setOnClickListener(onSend);
	}

	private OnClickListener onSend = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String num = numTV.getText().toString();
			String content = contentTV.getText().toString();
			
			boolean bSuccess = new Random().nextBoolean();
			int type = bSuccess ? MsgSQLiteOpenHelper.TYPE_SENT : MsgSQLiteOpenHelper.TYPE_UNSEND;
			long time = new Date().getTime()/ 1000;
			long serialnum = 0; //TODO
			boolean isPoint = false;
			BDMsg msg = new BDMsg(num, content, type, time, serialnum, isPoint);
			((SmsApplication)getApplication()).addBDMsg(msg);
			
			//real send stuff
			finish();
		}
	};

	public void onBackPressed()
	{
		if (!contentTV.getText().toString().equals("")) {
			saveDraftDialog = new AlertDialog.Builder(this)
					.setTitle(R.string.draft_save_title)
					.setMessage(R.string.draft_save_message)
					.setPositiveButton(R.string.save,
							new AlertDialog.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int which)
								{
									String num = numTV.getText().toString();
									String content = contentTV.getText().toString();
									
									int type = MsgSQLiteOpenHelper.TYPE_DRAFT;
									long time = new Date().getTime()/ 1000;
									long serialnum = 0; //TODO
									boolean isPoint = false;
									BDMsg msg = new BDMsg(num, content, type, time, serialnum, isPoint);
									((SmsApplication)getApplication()).addBDMsg(msg);
									finish();
								}
							})
					.setNeutralButton(R.string.discard,
							new AlertDialog.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int which)
								{
									finish();
								}
							})
					.setNegativeButton(R.string.cancel,
							new AlertDialog.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int which)
								{
									saveDraftDialog.cancel();
								}
							}).create();
			saveDraftDialog.show();
		}
		else{
			super.onBackPressed();
		}
	};
}
