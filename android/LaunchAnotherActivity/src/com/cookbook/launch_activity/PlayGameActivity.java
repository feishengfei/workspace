package com.cookbook.launch_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayGameActivity extends Activity
{

	private TextView tv;
	private int answer;
	private String author;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        setupView();
	}

	private void setupView()
	{
        tv = (TextView) findViewById(R.id.game_text);
        Intent intent = getIntent();
        answer = intent.getIntExtra("meaningOfLife", -1);
        author = intent.getStringExtra("userName");
        
        tv.setText(author + " : " + answer);
        
        answer = answer - 41;
        author = "Professor " + author;
        
        
        Button endButton = (Button)	findViewById(R.id.end_game);
        endButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                Intent i = getIntent();
                i.putExtra("returnInt", answer);
                i.putExtra("returnStr", author);
                setResult(RESULT_OK, i);
                finish();				
			}
		});
	}

}
