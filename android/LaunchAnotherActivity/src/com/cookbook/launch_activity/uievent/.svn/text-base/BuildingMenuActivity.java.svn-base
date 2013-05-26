package com.cookbook.launch_activity.uievent;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.launch_activity.R;

public class BuildingMenuActivity extends Activity
{
	private static final int MENU_ADD = 1;
	private static final int MENU_SEND = 2;
	private static final int MENU_DEL = 3;

	private static final int GROUP_DEFAULT = 0;
	private static final int GROUP_DEL = 1;
    
	private static final int ID_DEFAULT = 0;

	private static final int ID_TEXT1 = 1;
	private static final int ID_TEXT2 = 2;
	private static final int ID_TEXT3 = 3;

	private String[] choices = { 
			"Press me", 
			"Try Again", 
			"Change Me", 
    };

	private static TextView bv;
	private static int itemNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		setupView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(GROUP_DEFAULT, MENU_ADD, 0, "Add")
    		.setIcon(R.drawable.sunny);
		menu.add(GROUP_DEFAULT, MENU_SEND, 0, "Send");
		menu.add(GROUP_DEL, MENU_DEL, 0, "Delete");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if (itemNum > 0) {
			menu.setGroupVisible(GROUP_DEL, true);
		} else {
			menu.setGroupVisible(GROUP_DEL, false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case MENU_ADD:
                create_note();
				return true;
			case MENU_SEND:
                send_note();
				return true;
			case MENU_DEL:
                delete_note();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.menu_text) {
            SubMenu textMenu = menu.addSubMenu("Change Text");
            textMenu.add(0, ID_TEXT1, 0, choices[0]);
            textMenu.add(0, ID_TEXT2, 0, choices[1]);
            textMenu.add(0, ID_TEXT3, 0, choices[2]);
            menu.add(0, ID_DEFAULT, 0, "Original Text");
        }
	}
    
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
        switch (item.getItemId()) {
			case ID_DEFAULT:
				bv.setText(R.string.loading);
                return true;
			case ID_TEXT1: 
			case ID_TEXT2: 
			case ID_TEXT3: 
                bv.setText(choices[item.getItemId()-1]);
                return true;
			default:
				break;
		}
		return super.onContextItemSelected(item);
	}

	private void delete_note()
	{
        itemNum--;		
	}

	private void send_note()
	{
        Toast.makeText(this, "Item:" + itemNum, Toast.LENGTH_SHORT).show();
	}

	private void create_note()
	{
        itemNum++;
	}

	private void setupView()
	{
		bv = (TextView) findViewById(R.id.menu_text);
		registerForContextMenu((View) findViewById(R.id.menu_text));
	}
}
