package com.cookbook.layout_widgets;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cookbook.launch_activity.R;

public class DateTimeActivity extends Activity
{
    private static final int DATE_ID = 0;
    private static final int TIME_ID = 1;
    
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    
    DateFormat fmDateAndTime = DateFormat.getDateInstance();
    private TextView lbDateTime;
	private OnDateSetListener d = new OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth)
		{
            mYear = year;			
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateLabel();
		}
	};
	private OnTimeSetListener t = new OnTimeSetListener()
	{
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute)
		{
            mDay = hourOfDay;			
            mMinute = minute;
            updateLabel();
		}
	};
    
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.datetimepicker);
        setupView();
    }

	protected void updateLabel()
	{
        lbDateTime.setText(
                new StringBuilder()
                .append(mMonth + 1).append("-")
                .append(mDay).append("-")
                .append(mYear).append(" ")
                .append(pad(mHour)).append(":")
                .append(pad(mMinute))
        		);
	}

	private void setupView()
	{
        lbDateTime = (TextView)findViewById(R.id.date_and_time);
        Button dateBtn = (Button)findViewById(R.id.dateBtn);
        dateBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
		        showDialog(DATE_ID);
			}
		});
        
        Button timeBtn = (Button)findViewById(R.id.timeBtn);
        timeBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
		        showDialog(TIME_ID);
			}
		});
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        updateLabel();
	}
    
	@Override
	protected Dialog onCreateDialog(int id)
	{
        switch (id) {
			case DATE_ID:
	            return new DatePickerDialog(this, 
	            		d, mYear, mMonth, mDay);
			case TIME_ID:
                return new TimePickerDialog(this, 
                		t, mHour, mMinute, true);
		}
		return null;
	}
    
	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
        switch (id) {
			case DATE_ID:
				((DatePickerDialog)dialog).updateDate(mYear, mMonth, mDay);
				break;
			case TIME_ID:
                ((TimePickerDialog)dialog).updateTime(mHour, mMinute);
                break;
			default:
				break;
		} 
	}
    
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
