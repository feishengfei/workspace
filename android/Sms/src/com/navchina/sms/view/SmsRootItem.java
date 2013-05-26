package com.navchina.sms.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.navchina.R;
import com.navchina.sms.model.SmsRoot;

public class SmsRootItem extends LinearLayout
{
	private SmsRoot root;
	private ImageView root_icon;
	private TextView root_title;
	private TextView root_info;

	public SmsRootItem(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		root_icon = (ImageView) findViewById(R.id.root_icon);
		root_title = (TextView) findViewById(R.id.root_title);
		root_info = (TextView) findViewById(R.id.root_info);
	}

	public SmsRoot getRoot()
	{
		return root;
	}

	public void setRoot(SmsRoot root)
	{
		this.root = root;
		root_icon.setImageResource(root.getIcon());
		root_title.setText(root.getTitle());
		root_info.setText(root.getInfo());
	}
}
