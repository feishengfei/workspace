package com.cookbook.layout_widgets.list;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookbook.launch_activity.R;

public class MsgItem extends LinearLayout
{
	private Msg msg;
	private CheckBox msgCheck;
	private TextView msgContact;
	private TextView msgDate;
	private ImageView msgUnread;
	private TextView msgContent;

	public MsgItem(Context context)
	{
		super(context);
	}

	public MsgItem(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		msgCheck = (CheckBox) findViewById(R.id.msg_check);
		msgContact = (TextView) findViewById(R.id.msg_contact);
		msgDate = (TextView) findViewById(R.id.msg_date);
		msgUnread = (ImageView) findViewById(R.id.msg_unread);
		msgContent = (TextView) findViewById(R.id.msg_content);
		msgCheck.setFocusable(false);
		msgCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				msg.setChecked(isChecked);
			}
		});
	}

	public Msg getMsg()
	{
		return msg;
	}

	private String headStr(String orig, float textSize)
	{
		String ret = "";
		if (orig.contains("\n")) {
			ret = orig.substring(0, orig.indexOf("\n"));
			ret += "...";
		}
		else {
			ret = orig;
		}
		
		if (ret.length() > 10) {
			ret = ret.substring(0, 10);
			ret += "...";
		}
		return ret;
	}

	public void setMsg(Msg msg)
	{
		this.msg = msg;
		msgCheck.setChecked(msg.isChecked());
		msgContact.setText(msg.getContacts());
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date(msg.getTs() * 1000));
		msgDate.setText(time);
		msgUnread.setVisibility(msg.isUnread() ? View.VISIBLE : View.INVISIBLE);
		msgContent.setText(headStr(msg.getContent(), msgContent.getTextSize()));
	}

}
