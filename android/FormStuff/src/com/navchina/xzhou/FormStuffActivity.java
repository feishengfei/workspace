package com.navchina.xzhou;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FormStuffActivity extends Activity
{
	private EditText editText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupView();
	}

	private void setupView()
	{
		setContentView(R.layout.main);
		editText = (EditText) findViewById(R.id.edittext);
		editText.setOnKeyListener(onEnter);
		final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingbar);
		ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener()
		{

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser)
			{
				Toast.makeText(FormStuffActivity.this, "New Rating:" + rating,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void onCumtomButtonClicked(View v)
	{
		Toast.makeText(FormStuffActivity.this, "button clicked",
				Toast.LENGTH_SHORT).show();
	}

	public void onCheckboxClicked(View view)
	{
		if (((CheckBox) view).isChecked()) {
			Toast.makeText(FormStuffActivity.this, "Selected",
					Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(FormStuffActivity.this, "Not selected",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onRadioButtonClicked(View view)
	{
		RadioButton rb = (RadioButton) view;
		Toast.makeText(FormStuffActivity.this, rb.getText(), Toast.LENGTH_SHORT)
				.show();
	}

	public void onToggleClicked(View view)
	{
		if (((ToggleButton) view).isChecked()) {
			Toast.makeText(FormStuffActivity.this, "Toggle on",
					Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(FormStuffActivity.this, "Toggle off",
					Toast.LENGTH_SHORT).show();
		}
	}

	private OnKeyListener onEnter = new View.OnKeyListener()
	{
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event)
		{
			if (KeyEvent.ACTION_DOWN == event.getAction()
					&& KeyEvent.KEYCODE_ENTER == keyCode) {
				Toast.makeText(FormStuffActivity.this,
						((EditText) v).getText(), Toast.LENGTH_SHORT).show();
				return true;
			}
			return false;
		}
	};
}