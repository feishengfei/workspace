package com.navchina.spinner.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.navchina.spinner.HelloSpinnerActivity;

public class SpinnerActivityTest extends ActivityInstrumentationTestCase2
{
	private static final int ADAPTER_COUNT = 9;
	protected static final int INITIAL_POSITION = 0;
	private static final int TEST_POSITION = 5;
	private static final int TEST_STATE_DESTROY_SELECTION = 7;
	private Activity mActivity;
	private Spinner mSpinner;
	private SpinnerAdapter mPlanetData;
	private int mPos;
	private String mSelection;

	public SpinnerActivityTest()
	{
		super("com.navchina.spinner", HelloSpinnerActivity.class);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		mSpinner = (Spinner) mActivity
				.findViewById(com.navchina.spinner.R.id.planet_spinner);
		mPlanetData = mSpinner.getAdapter();
	}

	public void testPreConditions()
	{
		assertTrue(mSpinner.getOnItemSelectedListener() != null);
		assertTrue(mPlanetData != null);
		assertEquals(mPlanetData.getCount(), ADAPTER_COUNT);
	}

	public void testSpinnerUI() throws InterruptedException
	{
		mActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mSpinner.requestFocus();
				mSpinner.setSelection(INITIAL_POSITION);
			}
		});

		sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		for (int i = 0; i < TEST_POSITION; i++) {
			sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

		mPos = mSpinner.getSelectedItemPosition();
		mSelection = (String) mSpinner.getItemAtPosition(mPos);

		TextView resultView = (TextView) mActivity
				.findViewById(com.navchina.spinner.R.id.spinner_result);
		String resultText = resultView.getText().toString();

		assertEquals(mSelection, resultText);
	}

	public void testStateDestroy()
	{
		mSpinner.setSelection(TEST_STATE_DESTROY_SELECTION);
		mActivity.finish();
		mActivity = getActivity();
		
		int currentPosition = mSpinner.getSelectedItemPosition();
		Log.i("testStateDestroy:", ""+currentPosition);
		//assertEquals(currentPosition, TEST_STATE_DESTROY_SELECTION);
	}

}
