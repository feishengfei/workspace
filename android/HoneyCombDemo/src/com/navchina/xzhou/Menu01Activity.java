package com.navchina.xzhou;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Menu01Activity extends ListActivity
{
    public static final int MENU_ADD = Menu.FIRST + 1;
    public static final int MENU_RESET = Menu.FIRST + 2;
    public static final int MENU_CAP = Menu.FIRST + 3;
    public static final int MENU_REMOVE = Menu.FIRST + 4;
    private ArrayList<String> words = null;
    
    private TextView.OnEditorActionListener onSearch = 
    	new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
                addWord(v);   
                v.setText("");
                
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                
				return true;
			}
		};
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        initAdapter();
        registerForContextMenu(getListView());
        
        int change = getActionBar().getDisplayOptions() ^ ActionBar.DISPLAY_SHOW_TITLE;
        getActionBar().setDisplayOptions(change, ActionBar.DISPLAY_SHOW_TITLE);  
    }
    
    protected void addWord(TextView v)
	{
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)getListAdapter();
        
        adapter.add(v.getText().toString());
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        new MenuInflater(this).inflate(R.menu.options, menu);
        EditText add = (EditText)menu
                            .findItem(R.id.add)
                            .getActionView()
                            .findViewById(R.id.menu01_add_text);
        add.setOnEditorActionListener(onSearch);
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
        	case R.id.add:
        		add();
        		return true;
        	case MENU_RESET:
        	case R.id.reset:
        		initAdapter();
                return true;
        	case android.R.id.home:
        	case R.id.about:
        		about();
                return true;
        }
    	return super.onOptionsItemSelected(item);
    }
    
    private void about()
	{
        Toast.makeText(this, R.string.app_about, Toast.LENGTH_SHORT).show();
	}

	private void add()
	{
	    final View addView = 
	    	getLayoutInflater().inflate(R.layout.menu01_add, null);
        
	    new AlertDialog.Builder(this)
        .setTitle(R.string.add_a_word)
        .setView(addView)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
                addWord((EditText)addView.findViewById(R.id.menu01_add_text));
			}
		})
        .setNegativeButton(R.string.cancel, null)
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
        for (String s : getResources().getStringArray(R.array.beidou)) {
			words.add(s);
		}
        
        setListAdapter(new ArrayAdapter<String>(this, 
        		android.R.layout.simple_list_item_1, words));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
        Toast.makeText(this, 
        		words.get(position), Toast.LENGTH_SHORT)
        		.show();
	}
    
    
}

