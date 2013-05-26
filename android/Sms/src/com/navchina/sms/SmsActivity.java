package com.navchina.sms;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.navchina.R;
import com.navchina.contact.ContactsActivity;
import com.navchina.sms.adapter.SmsRootAdapter;
import com.navchina.sms.model.SmsRoot;

public class SmsActivity extends ListActivity
{
	private static final int OPTION_SETTINGS = 0;
	private static final int OPTION_CONTACT = 1;
	private static final String TAG = "Sms Activity";
	private AtomicBoolean bFirstBackPressed = new AtomicBoolean(false);
	private SmsRoot writeNew;
	private SmsRoot nodeWriteNew;
	private SmsRoot nodeInbox;
	private SmsRoot nodeUnsend;
	private SmsRoot nodeSent;
	private SmsRoot nodeDraft;
	private SmsRoot nodeTemplate;
	private SmsRoot nodeServMsg;
	private SmsApplication app;
	private SmsRootAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		app = (SmsApplication) getApplication();
		super.onCreate(savedInstanceState);
		adapter = (SmsRootAdapter)getListAdapter();
		setupView();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		int count = 0;
		//update unsend
		count = app.getUnsendCount();
		Log.i(TAG, "unsent count:" + count);
		if (0 == count) {
			nodeUnsend.setInfo("");
		}
		else {
			nodeUnsend.setInfo("(" + count + ")");
		}
		
		//update unread
		count = app.getUnreadCount();
		Log.i(TAG, "unread count:" + count);
		if (0 == count) {
			nodeInbox.setInfo("");
		}
		else {
			nodeInbox.setInfo("(" + count + ")");
		}
		adapter.notifyDataSetChanged();
	}

	private void setupView()
	{
		setContentView(R.layout.main);
		setTitle(R.string.app_name);
		adapter = new SmsRootAdapter(createRoots(), this);
		setListAdapter(adapter);
	}

	private ArrayList<SmsRoot> createRoots()
	{
		int i = 0;
		String[] nodeTitles = getResources().getStringArray(R.array.sms_root);
		ArrayList<SmsRoot> ret = new ArrayList<SmsRoot>();

		nodeWriteNew = new SmsRoot();
		nodeWriteNew.setTitle(nodeTitles[i++]);

		nodeInbox = new SmsRoot();
		nodeInbox.setTitle(nodeTitles[i++]);

		nodeUnsend = new SmsRoot();
		nodeUnsend.setTitle(nodeTitles[i++]);

		nodeSent = new SmsRoot();
		nodeSent.setTitle(nodeTitles[i++]);

		nodeDraft = new SmsRoot();
		nodeDraft.setTitle(nodeTitles[i++]);

		nodeServMsg = new SmsRoot();
		nodeServMsg.setTitle(nodeTitles[i++]);

		nodeTemplate = new SmsRoot();
		nodeTemplate.setTitle(nodeTitles[i++]);

		ret.add(nodeWriteNew);
		ret.add(nodeInbox);
		ret.add(nodeUnsend);
		ret.add(nodeSent);
		ret.add(nodeDraft);
		ret.add(nodeServMsg);
		ret.add(nodeTemplate);
		return ret;
	}

	@Override
	public void onBackPressed()
	{
		if (!bFirstBackPressed.get()) {
			Toast.makeText(this, R.string.press_again_to_quit,
					Toast.LENGTH_SHORT).show();
			bFirstBackPressed.set(true);
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try {
						Thread.sleep(3000);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
					bFirstBackPressed.set(false);
				}
			}).start();
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, OPTION_SETTINGS, 0, R.string.settings);
		menu.add(0, OPTION_CONTACT, 0, R.string.contacts);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		switch (position) {
			case 0:
				startActivity(new Intent(this, SmsWriteActivity.class));
				break;
			case 1:
				startActivity(new Intent(this, InboxActivity.class));
				break;
			case 2:
				startActivity(new Intent(this, UnsendBoxActivity.class));
				break;
			case 3:
				startActivity(new Intent(this, SentBoxActivity.class));
				break;
			case 4:
				startActivity(new Intent(this, DraftBoxActivity.class));
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case OPTION_CONTACT:
				startActivity(new Intent(this, ContactsActivity.class));
				return true;
			case OPTION_SETTINGS:
				// TODO
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}