package com.navchina.sms.view;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.navchina.R;
import com.navchina.sms.model.BDMsg;
import com.navchina.sms.model.MsgSQLiteOpenHelper;

public class MsgItem extends LinearLayout
{
	private BDMsg msg;
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

	public BDMsg getMsg()
	{
		return msg;
	}

	// XXX
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

	public void setMsg(BDMsg msg)
	{
		this.msg = msg;
		msgCheck.setChecked(msg.isChecked());
		msgContact.setText(msg.getNum());
		msgDate.setText(formatTime(msg.getTime()));
		msgUnread
				.setVisibility(msg.getType() == MsgSQLiteOpenHelper.TYPE_UNREAD ? View.VISIBLE
						: View.INVISIBLE);
		msgContent.setText(headStr(msg.getContent(), msgContent.getTextSize()));
	}

	/**
	 * @param time
	 *            以秒为单位
	 */
	private String formatTime(long time)
	{
		String ret = "";
		Date d = new Date(time * 1000);
		Date now = new Date();
		if (d.getYear() != now.getYear()) {
			int year = d.getYear() + 1900;
			ret = "" + year + "年";
		}
		else if (d.getMonth() != now.getMonth()) {
			ret = "" + (1 + d.getMonth()) + "月" + d.getDate() + "日";
		}
		else if (d.getDate() != now.getDate()) {
			ret = "" + (1 + d.getMonth()) + "月" + d.getDate() + "日 "
					+ d.getHours() + ":" + d.getMinutes();
		}
		else {
			ret = "" + d.getHours() + ":" + d.getMinutes();
		}
		return ret;
	}

}
