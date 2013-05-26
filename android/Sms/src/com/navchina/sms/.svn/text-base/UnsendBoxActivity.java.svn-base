package com.navchina.sms;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.navchina.R;
import com.navchina.sms.adapter.MsgAdapter;
import com.navchina.sms.model.BDMsg;
import com.navchina.sms.model.MsgSQLiteOpenHelper;

public class UnsendBoxActivity extends ListActivity
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
	private SmsApplication app;
	private MsgAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_unsend);
		setupView();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	private void setupView()
	{
		setTitle(R.string.unsendbox);
		app = (SmsApplication)getApplication();
		adapter = new MsgAdapter(app.getUnsendMsgs(), this);
		setListAdapter(adapter);
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
				app.deleteMsgs(adapter.deleteCheckedMsg());
				return true;
			case OPTION_FORWARD:
				// TODO
				break;
			case OPTION_REPLY:
				// TODO
				break;
			case OPTION_SAVENUMBER:
				// TODO
				break;
			case OPTION_SELECT_ALL:
				adapter.checkAll(true);
				return true;
			case OPTION_DESELECT_ALL:
				adapter.checkAll(false);
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
		BDMsg msg = (BDMsg) adapter.getItem(position);
		Intent intent = new Intent(this, SmsDetailActivity.class);
		intent.putExtra("id", msg.getId());
		intent.putExtra("num", msg.getNum());
		intent.putExtra("date", msg.getTime());
		intent.putExtra("content", msg.getContent());
		intent.putExtra("forward", true);
		intent.putExtra("reply", true);
		intent.putExtra("delete", true);
		startActivity(intent);
	}
}
