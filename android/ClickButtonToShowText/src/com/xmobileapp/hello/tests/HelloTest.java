package com.xmobileapp.hello.tests;

import android.content.Intent;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xmobileapp.hello.Hello;
import com.xmobileapp.hello.R;

public class HelloTest extends InstrumentationTestCase {
	
	Hello mActivityTested;
	
	public HelloTest() {
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		intent.setClassName("com.xmobileapp.hello", Hello.class.getName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mActivityTested = (Hello) getInstrumentation().startActivitySync(intent);
	}

	@Override
	protected void tearDown() throws Exception {
		mActivityTested.finish();
		super.tearDown();
	}
	
	public void testClickButtonToShowText() throws Exception {
		TextView tv = (TextView) mActivityTested.findViewById(R.id.text);
		SystemClock.sleep(2000);
		assertEquals("TextView should be Gone before Button Clicking",
				View.GONE, tv.getVisibility());
		
		Button btn = (Button) mActivityTested.findViewById(R.id.button);
		getInstrumentation().runOnMainSync(new PerformClick(btn));
		SystemClock.sleep(2000);
		assertEquals("TextView should be Visible after Button Clicking",
				View.VISIBLE, tv.getVisibility());		
	}
	
	private class PerformClick implements Runnable {
		Button mBtnClicked;
		
		public PerformClick(Button button) {
			mBtnClicked = button;
		}
		
		public void run() {
			mBtnClicked.performClick();
		}
	}

}
