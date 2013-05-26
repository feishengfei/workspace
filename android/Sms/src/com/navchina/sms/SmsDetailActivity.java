package com.navchina.sms;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.navchina.R;

public class SmsDetailActivity extends Activity
{
	private long id;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupView();
	}

	private void setupView()
	{
		setContentView(R.layout.sms_detail);
		
		//ui
		TextView num = (TextView) findViewById(R.id.sms_detail_num);
		TextView date = (TextView) findViewById(R.id.sms_detail_date);
		TextView content = (TextView) findViewById(R.id.sms_detail_content);
		Button deleteBtn = (Button) findViewById(R.id.sms_detail_btn_delete);
		Button replyBtn = (Button) findViewById(R.id.sms_detail_btn_reply);
		Button forwardBtn = (Button) findViewById(R.id.sms_detail_btn_forward);

		//intent
		Intent intent = getIntent();
		boolean isDeleteAble = intent.getBooleanExtra("delete", false);
		boolean isForwardAble = intent.getBooleanExtra("forward", false);
		boolean isReplyAble = intent.getBooleanExtra("reply", false);
		long ts = intent.getLongExtra("date", 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(new Date(ts * 1000));
		id = intent.getLongExtra("id", 0);

		num.setText(intent.getStringExtra("num"));
		date.setText(dateStr);
		content.setText(intent.getStringExtra("content"));

		deleteBtn.setOnClickListener(onDelete);
		replyBtn.setOnClickListener(onReply);
		forwardBtn.setOnClickListener(onForward);
		
		deleteBtn.setEnabled(isDeleteAble);
		replyBtn.setEnabled(isReplyAble);
		forwardBtn.setEnabled(isForwardAble);

	}

	private OnClickListener onDelete = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			((SmsApplication)getApplication()).deleteMsgs(new Long[]{id});
			finish();
		}
	};

	private OnClickListener onForward = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			TextView tv = (TextView)SmsDetailActivity.this.findViewById(R.id.sms_detail_content);
			String content = tv.getText().toString();
			Intent intent = new Intent(SmsDetailActivity.this, SmsWriteActivity.class);
			intent.putExtra("content", content);
			startActivity(intent);
			finish();
		}
	};
	
	private OnClickListener onReply = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			TextView tv = (TextView)SmsDetailActivity.this.findViewById(R.id.sms_detail_num);
			String num = tv.getText().toString();
			Intent intent = new Intent(SmsDetailActivity.this, SmsWriteActivity.class);
			intent.putExtra("num", num);
			startActivity(intent);
			finish();
		}
	};
}
