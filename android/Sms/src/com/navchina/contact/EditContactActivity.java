package com.navchina.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.navchina.R;

public class EditContactActivity extends Activity
{
	private String oldname;
	private String number;
	private EditText nameEdit;
	private EditText numberEdit;
	private long id;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_edit_text_entry);
		setupView();
	}

	private void setupView()
	{
		Intent intent = getIntent();
		id = intent.getLongExtra("id", 0);
		oldname = intent.getStringExtra("oldname");
		number = intent.getStringExtra("number");
		
		nameEdit = (EditText)findViewById(R.id.contacts_name_edit);
		nameEdit.setText(oldname);
		numberEdit = (EditText)findViewById(R.id.contacts_number_edit);
		numberEdit.setText(number);
		
		Button okBtn = (Button)findViewById(R.id.contacts_ok);
		okBtn.setOnClickListener(onOk);
		
		Button cancelBtn = (Button)findViewById(R.id.contacts_cancel);
		cancelBtn.setOnClickListener(onCancel);
	}
	
	private OnClickListener onOk = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
            Intent i = getIntent();
            i.putExtra("id", id);
            i.putExtra("oldname", oldname);
            i.putExtra("name", nameEdit.getText().toString());
            i.putExtra("number", numberEdit.getText().toString());
            setResult(RESULT_OK, i);
            finish();			
		}
	};
	
	private OnClickListener onCancel = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};
}
