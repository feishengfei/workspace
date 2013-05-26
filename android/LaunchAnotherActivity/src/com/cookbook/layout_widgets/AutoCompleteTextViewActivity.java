package com.cookbook.layout_widgets;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.cookbook.launch_activity.R;


public class AutoCompleteTextViewActivity extends Activity
{
    private static final String[] items = {
        "lorem", "ipsum", "dolor",
        "sit", "amet",
        "consectetuer", "adipiscing", "elit", "morbi", "vel",
        "ligula", "vitae", "arcu", "aliquet", "mollis",
        "etiam", "vel", "erat", "placerat", "ante",
        "porttitor", "sodales", "pellentesque", "augue", "purus"
    };
	private TextView selection;
	private AutoCompleteTextView edit;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.autocompletetextview);
        setupView();
    }

	private void setupView()
	{
	    selection = (TextView)findViewById(R.id.actv_sel);
        edit = (AutoCompleteTextView)findViewById(R.id.actv);
        edit.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
                selection.setText(edit.getText());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});
        edit.setAdapter( new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, items));
        
	}
}
