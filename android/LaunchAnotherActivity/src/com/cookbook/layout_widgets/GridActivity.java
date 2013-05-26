package com.cookbook.layout_widgets;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.launch_activity.R;

public class GridActivity extends Activity implements OnItemSelectedListener
{
    private TextView selection;
    private static final String[] items = {
        "lorem", "ipsum", "dolor",
        "lorem", "ipsum", "dolor",
        "lorem", "ipsum", "dolor",
        "lorem", "ipsum", "dolor",
        "lorem", "ipsum", "dolor",
        "lorem", "ipsum", "dolor",
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        setupView();
    }

	private void setupView()
	{
        selection = (TextView)findViewById(R.id.grid_text);
        selection.setText("grid demo");
        
        GridView g = (GridView)findViewById(R.id.grid);
        g.setOnItemSelectedListener(this);
        g.setAdapter(new ArrayAdapter<String>(this, 
        		R.layout.cell, items));
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3)
	{
        selection.setText(items[arg2]);
        Toast.makeText(this, 
        		items[arg2] + " clicked " + arg2, Toast.LENGTH_SHORT);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
        selection.setText("");
	}
}
