package com.cookbook.layout_widgets.list;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.cookbook.launch_activity.R;

public class InboxActivity extends ListActivity
{
	private static final int OPTION_REPLY = 1;
	private static final int OPTION_FORWARD = 2;
	private static final int OPTION_DELETE = 3;
	private static final int OPTION_SAVENUMBER = 4;
	private static final int OPTION_SELECT_ALL = 5;
	private static final int OPTION_DESELECT_ALL = 6;

	private static final int GROUP_NONE = 0;
	private static final int GROUP_ONE = 1;
	private static final int GROUP_ONEMORE = 2;

	/*
	private Button menuBtn;
	private Button backBtn;
	*/
	private ArrayList<Msg> msgs;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		setupView();
		msgs = genMsgs();
		setListAdapter(new MsgAdapter(msgs, this));
	}

	private void setupView()
	{
		setTitle(R.string.inbox);
		/*
		 * menuBtn = (Button) findViewById(R.id.inbox_menu); backBtn = (Button)
		 * findViewById(R.id.inbox_back); menuBtn.setOnClickListener(onMenu);
		 * backBtn.setOnClickListener(onBack);
		 */
	}

	private ArrayList<Msg> genMsgs()
	{
		Random rand = new Random();
		ArrayList<Msg> msgs = new ArrayList<Msg>();
		for (int i = 0; i < 20; i++) {
			msgs.add(genMsg(rand));
		}
		return msgs;
	}

	private Msg genMsg(Random rand)
	{
		String[] name = { "张三", "李四", "王五", "周六", "赵七", "钱八", };
		int ts = (int) new Date().getTime();
		Msg msg = new Msg();
		msg.setContacts(name[rand.nextInt(name.length)]);
		msg.setUnread(true);
		msg.setContent("您好\n世界\nHelloWorld");
		msg.setTs(rand.nextInt(ts) / 1000);
		return msg;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(GROUP_ONE, OPTION_REPLY, 0, R.string.reply);
		menu.add(GROUP_ONE, OPTION_FORWARD, 0, R.string.forward);
		menu.add(GROUP_ONEMORE, OPTION_DELETE, 0, R.string.delete);
		menu.add(GROUP_ONE, OPTION_SAVENUMBER, 0, R.string.save_number);
		menu.add(GROUP_NONE, OPTION_SELECT_ALL, 0, R.string.select_all);
		menu.add(GROUP_ONEMORE, OPTION_DESELECT_ALL, 0, R.string.deselect_all);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		MsgAdapter adapter = (MsgAdapter) getListAdapter();
		if (adapter.getCheckedCount() == 0) {
			menu.setGroupVisible(GROUP_NONE, true);
			menu.setGroupVisible(GROUP_ONE, false);
			menu.setGroupVisible(GROUP_ONEMORE, false);
		}
		else if (adapter.getCheckedCount() == 1) {
			menu.setGroupVisible(GROUP_NONE, false);
			menu.setGroupVisible(GROUP_ONE, true);
			menu.setGroupVisible(GROUP_ONEMORE, true);
		}
		else {
			menu.setGroupVisible(GROUP_NONE, false);
			menu.setGroupVisible(GROUP_ONE, false);
			menu.setGroupVisible(GROUP_ONEMORE, true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case OPTION_DELETE:
				((MsgAdapter) getListAdapter()).deleteCheckedMsg();
				return true;
			case OPTION_FORWARD:
				break;
			case OPTION_REPLY:
				break;
			case OPTION_SAVENUMBER:
				break;
			case OPTION_SELECT_ALL:
				((MsgAdapter) getListAdapter()).checkAll(true);
				return true;
			case OPTION_DESELECT_ALL:
				((MsgAdapter) getListAdapter()).checkAll(false);
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		// TODO
	}

	private OnClickListener onMenu = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			openOptionsMenu();
		}
	};

	private OnClickListener onBack = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};
}
