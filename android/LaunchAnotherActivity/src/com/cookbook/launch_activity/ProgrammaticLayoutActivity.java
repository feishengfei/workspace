package com.cookbook.launch_activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProgrammaticLayoutActivity extends Activity
{
	private static int TEXTVIEW1_ID = 100011;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupView();
	}

	private void setupView()
	{
		final RelativeLayout relLayout = new RelativeLayout(this);
		relLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		TextView textView1 = new TextView(this);
		textView1.setText("middle");
		textView1.setTag(TEXTVIEW1_ID);

		RelativeLayout.LayoutParams text1layout = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		text1layout.addRule(RelativeLayout.CENTER_IN_PARENT);
		relLayout.addView(textView1, text1layout);

		TextView textView2 = new TextView(this);
		textView2.setText("high");
		RelativeLayout.LayoutParams text2Layout = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		text2Layout.addRule(RelativeLayout.ABOVE, TEXTVIEW1_ID);
		relLayout.addView(textView2, text2Layout);

		setContentView(relLayout);
	}

}
