package com.cookbook.layout_widgets.list;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class MySimpleCursorAdapter extends CursorAdapter 
{


	public MySimpleCursorAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
