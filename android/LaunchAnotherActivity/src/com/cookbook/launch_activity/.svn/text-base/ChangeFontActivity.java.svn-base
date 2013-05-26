package com.cookbook.launch_activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChangeFontActivity extends Activity
{
	private TextView tv;
	protected int cid = 0;
	protected int sid = 0;
    protected int contentId = 0;
    private static final int COLOR_VALS[] = {
        R.color.start,	
        R.color.mid,	
        R.color.last,	
    };
    private static final int FONT_SIZES[] = {
        R.dimen.small,	
        R.dimen.medium,	
        R.dimen.large,	
    };
    private static final int CONTENTS[] = {
        R.string.changed_text1,
        R.string.changed_text2,
        R.string.changed_text3,
    };
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.changefont);
        setupView();
	}

	private void setupView()
	{
        tv = (TextView)findViewById(R.id.mod_text);
        
        Button changeFont = (Button)findViewById(R.id.change_color);
        changeFont.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			    tv.setTextColor(getResources().getColor(COLOR_VALS[(++cid % 3)]));
			}
		});
        
        Button changeSize = (Button)findViewById(R.id.change_size);
        changeSize.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                tv.setTextSize(getResources().getDimension(FONT_SIZES[(++sid % 3)]));
			}
		});
        
        Button changeContent = (Button)findViewById(R.id.change_content);
        changeContent.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                tv.setText(getResources().getString(CONTENTS[(++contentId % 3)]));
			}
		});
        
	}
    
}
