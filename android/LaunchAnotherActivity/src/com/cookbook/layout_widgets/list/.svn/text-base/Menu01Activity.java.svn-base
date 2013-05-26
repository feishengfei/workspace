package com.cookbook.layout_widgets.list;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.cookbook.launch_activity.R;

public class Menu01Activity extends ListActivity
{
    private static final String[] items = {
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday",
    };
    public static final int MENU_ADD = Menu.FIRST + 1;
    public static final int MENU_RESET = Menu.FIRST + 2;
    public static final int MENU_CAP = Menu.FIRST + 3;
    public static final int MENU_REMOVE = Menu.FIRST + 4;
    private ArrayList<String> words = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        initAdapter();
        registerForContextMenu(getListView());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        /*
        menu.add(Menu.NONE, MENU_ADD, Menu.NONE, "Add")
        .setIcon(android.R.drawable.ic_menu_add);
        
        menu.add(Menu.NONE, MENU_RESET, Menu.NONE, "Reset")
        .setIcon(android.R.drawable.ic_menu_recent_history);
        */
        new MenuInflater(this).inflate(R.menu.option_menu, menu);
        
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo)
    {
        /*
        menu.add(Menu.NONE, MENU_CAP, Menu.NONE, "大写");
        menu.add(Menu.NONE, MENU_REMOVE, Menu.NONE, "移除");
        */
        new MenuInflater(this).inflate(R.menu.context_menu, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
        	case MENU_ADD:
        	case R.id.option_menu_add:
        		add();
        		return true;
        	case MENU_RESET:
        	case R.id.option_menu_refresh:
        		initAdapter();
                return true;
        }
    	return super.onOptionsItemSelected(item);
    }
    
    private void add()
	{
	    final View addView = 
	    	getLayoutInflater().inflate(R.layout.menu01_add, null);
        
	    new AlertDialog.Builder(this)
        .setTitle("Add a Word")
        .setView(addView)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				ArrayAdapter<String> adapter 
    				= (ArrayAdapter<String>)getListAdapter();
                EditText title = (EditText)addView.findViewById(R.id.menu01_add_text);
                
                adapter.add(title.getText().toString());
                
			}
		})
        .setNegativeButton("Cancel", null)
        .show();
	}

	@Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = 
        	(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)getListAdapter();
        long a = 10l;
        switch (item.getItemId()) {
			case MENU_CAP:
			case R.id.context_menu_cap:
				String word = words.get(info.position);
                word = word.toUpperCase();
                
                adapter.remove(words.get(info.position));
                adapter.insert(word, info.position);
                return(true);
			case MENU_REMOVE:
			case R.id.context_menu_rm:
				adapter.remove(words.get(info.position));
                return(true);
			default:
				break;
		} 
    	return super.onContextItemSelected(item);
    }

	private void initAdapter()
	{
        words = new ArrayList<String>();		
        for (String s : items) {
			words.add(s);
		}
        
        setListAdapter(new ArrayAdapter<String>(this, 
        		android.R.layout.simple_list_item_1, words));
	}
}

