package com.cookbook.launch_activity.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.cookbook.launch_activity.R;

public class ReadWriteFileDemo extends Activity
{
	private final static String NOTES = "notes.txt";
	private EditText editor;
	private File file;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textview2file);
		setupView();
	}

	private void setupView()
	{
		editor = (EditText) findViewById(R.id.edittext_2_file);
        file = new File(getExternalFilesDir(null), "note.txt");
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		try {
//			InputStream in = openFileInput(NOTES);
            InputStream in = new FileInputStream(file);

			if (null != in) {
				InputStreamReader tmp = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(tmp);
				String str;
				StringBuilder buf = new StringBuilder();

				while ((str = reader.readLine()) != null) {
					buf.append(str + "\n");
				}
				in.close();
				editor.setText(buf.toString());
			}
		}
		catch (Exception e) {
			Toast.makeText(this, "onRersume:" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		try {
			Toast.makeText(this, "save to:" + openFileInput(NOTES).getFD(),
					Toast.LENGTH_SHORT).show();
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
			out.write(editor.getText().toString());
			out.flush();
			out.close();
		}
		catch (Exception e) {
			Toast.makeText(this, "onPause:" + e.toString(), Toast.LENGTH_SHORT)
					.show();
		}
	}

}
