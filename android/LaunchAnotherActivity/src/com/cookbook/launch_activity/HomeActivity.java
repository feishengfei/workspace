package com.cookbook.launch_activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cookbook.launch_activity.db.ContactActivity;
import com.cookbook.launch_activity.io.ReadWriteFileDemo;
import com.cookbook.launch_activity.uievent.BuildingMenuActivity;
import com.cookbook.launch_activity.uievent.PhysicalKeyPressActivity;
import com.cookbook.layout_widgets.AlertDialogActivity;
import com.cookbook.layout_widgets.AutoCompleteTextViewActivity;
import com.cookbook.layout_widgets.BDI_Activity;
import com.cookbook.layout_widgets.BrowserActivity;
import com.cookbook.layout_widgets.ButtonReverseActivity;
import com.cookbook.layout_widgets.CheckBoxActivity;
import com.cookbook.layout_widgets.ClockActivity;
import com.cookbook.layout_widgets.DateTimeActivity;
import com.cookbook.layout_widgets.FingerBallActivity;
import com.cookbook.layout_widgets.Flipper01Activity;
import com.cookbook.layout_widgets.Flipper02Activity;
import com.cookbook.layout_widgets.GridActivity;
import com.cookbook.layout_widgets.ImageButtonActivity;
import com.cookbook.layout_widgets.ImageViewActivity;
import com.cookbook.layout_widgets.LayoutFrameActivity;
import com.cookbook.layout_widgets.OneButtonActivity;
import com.cookbook.layout_widgets.ProgressBar02Activity;
import com.cookbook.layout_widgets.ProgressBarActivity;
import com.cookbook.layout_widgets.RadioButtonActivity;
import com.cookbook.layout_widgets.SeekBarActivity;
import com.cookbook.layout_widgets.SimpleMathServiceDemo;
import com.cookbook.layout_widgets.SlidingDrawerActivity;
import com.cookbook.layout_widgets.SpinnerActivity;
import com.cookbook.layout_widgets.SpinnerActivity2;
import com.cookbook.layout_widgets.Tab01Activity;
import com.cookbook.layout_widgets.Tab02Activity;
import com.cookbook.layout_widgets.ToggleButtonActivity;
import com.cookbook.layout_widgets.list.InboxActivity;
import com.cookbook.layout_widgets.list.List1Activity;
import com.cookbook.layout_widgets.list.List2Activity;
import com.cookbook.layout_widgets.list.List3Activity;
import com.cookbook.layout_widgets.list.List4Activity;
import com.cookbook.layout_widgets.list.List5Activity;
import com.cookbook.layout_widgets.list.List6Activity;
import com.cookbook.layout_widgets.list.List7Activity;
import com.cookbook.layout_widgets.list.Menu01Activity;
import com.cookbook.layouts.LinearLayoutActivity;
import com.cookbook.layouts.ListViewActivity;

public class HomeActivity extends ListActivity
{
	private static final String[] ACTIVITYES = { "chap1_4", "Something else",
			"Image Button", "CheckBox", "radioButton", "ToggleButton",
			"Spinner", "ProgressBar", "SeekBar", "Physical Key Press",
			"Building Menus", "One Button", "Button Reverse", "Image View",
			"Linear Layout", "Listview(Month names of 1 year)", "Spinner 2",
			"Grid Demo", "AutoTextCompleteView", "List 1", "Date Time Picker",
			"Clock", "Tab01", "Tab02", "Flipper 01", "Flipper 02",
			"Slidding Drawer", "Browser", "Menu 01", "Show AlertDialog",
			"Rotation Demo ", "Progress Bar 02", "Test Intent",
			"List from arrays.xml", "List from diy xml", "Readin & Writin",
			"Simple Math Service Demo(aidl)", "BDI Activity Test",
			"List from cursor(from People)", "List 5", "Contact Activity",
			"List 6 CommonData", "List 7 SimpleCursorAdapter",
			"Inbox Activity", "Finger Ball Activity", "Layout Frame Activity", };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupViews();

	}

	private void setupViews()
	{
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, ACTIVITYES));

		ListView lv = getListView();
		lv.setOnItemClickListener(new ListView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				switch (position) {
					case 0:
						showChap1_4();
						break;
					case 1:
						showSomethingElse();
						break;
					case 2:
						showImageButtonActivity();
						break;
					case 3:
						showCheckBoxActivity();
						break;
					case 4:
						showRadioButtonActivity();
						break;
					case 5:
						showToggleButtonactivity();
						break;
					case 6:
						showSpinnerActivity();
						break;
					case 7:
						showProgressBarActivity();
						break;
					case 8:
						showSeekBarActivity();
						break;
					case 9:
						showPhysicalKeyPressActivity();
						break;
					case 10:
						showBuildingMenuActivity();
						break;
					case 11:
						showOneButtonActivity();
						break;
					case 12:
						showButtonReverseActivity();
						break;
					case 13:
						showImageViewActivity();
						break;
					case 14:
						showLinearLayoutActivity();
						break;
					case 15:
						showListViewActivity();
						break;
					case 16:
						showSpinnerActivity2();
						break;
					case 17:
						showGridActivity();
						break;
					case 18:
						showAutoTextCompleteViewActivity();
						break;
					case 19:
						showList1Activity();
						break;
					case 20:
						showDateTimeActivity();
						break;
					case 21:
						showClockActivity();
						break;
					case 22:
						showTab01Activity();
						break;
					case 23:
						showTab02Activity();
						break;
					case 24:
						showFlipper01Activity();
						break;
					case 25:
						showFlipper02Activity();
						break;
					case 26:
						showSlidingDrawerActivity();
						break;
					case 27:
						showBrowserActivity();
						break;
					case 28:
						showMenu01Activity();
						break;
					case 29:
						showAlertDialogActivity();
						break;
					case 30:
						showRoatitionActivity();
						break;
					case 31:
						showProgressBar02Activity();
						break;
					case 32:
						showTestIntentActivity();
						break;
					case 33:
						showList2Activity();
						break;
					case 34:
						shwwList3Activity();
						break;
					case 35:
						showReadWriteFileDemo();
						break;
					case 36:
						showSimpleMathServiceDemo();
						break;
					case 37:
						showBDIActivity();
						break;
					case 38:
						showPeopleListActivity();
						break;
					case 39:
						showList5Activity();
						break;
					case 40:
						showContactActivity();
						break;
					case 41:
						showList6Activity();
						break;
					case 42:
						showList7Activity();
						break;
					case 43:
						showInboxActivity();
						break;
					case 44:
						showFingerBallActivity();
						break;
					case 45:
						showLayoutFrameActivity();
						break;
					default:
						break;
				}
			}
		});
	}

	protected void showLayoutFrameActivity()
	{
		startActivity(new Intent(this, LayoutFrameActivity.class));
	}

	protected void showFingerBallActivity()
	{
		startActivity(new Intent(this, FingerBallActivity.class));
	}

	protected void showInboxActivity()
	{
		startActivity(new Intent(this, InboxActivity.class));
	}

	protected void showList7Activity()
	{
		startActivity(new Intent(this, List7Activity.class));
	}

	protected void showList6Activity()
	{
		startActivity(new Intent(this, List6Activity.class));
	}

	protected void showContactActivity()
	{
		startActivity(new Intent(this, ContactActivity.class));
	}

	protected void showList5Activity()
	{
		startActivity(new Intent(this, List5Activity.class));
	}

	protected void showPeopleListActivity()
	{
		startActivity(new Intent(this, List4Activity.class));
	}

	protected void showBDIActivity()
	{
		startActivity(new Intent(this, BDI_Activity.class));
	}

	protected void showSimpleMathServiceDemo()
	{
		startActivity(new Intent(this, SimpleMathServiceDemo.class));
	}

	protected void showReadWriteFileDemo()
	{
		startActivity(new Intent(this, ReadWriteFileDemo.class));
	}

	protected void shwwList3Activity()
	{
		startActivity(new Intent(this, List3Activity.class));
	}

	protected void showList2Activity()
	{
		Intent intent = new Intent(this, List2Activity.class);
		startActivity(intent);
	}

	protected void showTestIntentActivity()
	{
		Intent i = new Intent(this, TestIntentActivity.class);
		startActivity(i);
	}

	protected void showProgressBar02Activity()
	{
		Intent i = new Intent(this, ProgressBar02Activity.class);
		startActivity(i);
	}

	protected void showRoatitionActivity()
	{
		Intent i = new Intent(this, RotationActivity.class);
		startActivity(i);
	}

	protected void showAlertDialogActivity()
	{
		Intent i = new Intent(this, AlertDialogActivity.class);
		startActivity(i);
	}

	protected void showMenu01Activity()
	{
		Intent i = new Intent(this, Menu01Activity.class);
		startActivity(i);
	}

	protected void showBrowserActivity()
	{
		Intent i = new Intent(this, BrowserActivity.class);
		startActivity(i);
	}

	protected void showSlidingDrawerActivity()
	{
		Intent i = new Intent(this, SlidingDrawerActivity.class);
		startActivity(i);
	}

	protected void showFlipper02Activity()
	{
		Intent i = new Intent(this, Flipper02Activity.class);
		startActivity(i);
	}

	protected void showFlipper01Activity()
	{
		Intent i = new Intent(this, Flipper01Activity.class);
		startActivity(i);
	}

	protected void showTab02Activity()
	{
		Intent i = new Intent(this, Tab02Activity.class);
		startActivity(i);
	}

	protected void showTab01Activity()
	{
		Intent i = new Intent(this, Tab01Activity.class);
		startActivity(i);
	}

	protected void showClockActivity()
	{
		Intent i = new Intent(this, ClockActivity.class);
		startActivity(i);
	}

	protected void showDateTimeActivity()
	{
		Intent i = new Intent(this, DateTimeActivity.class);
		startActivity(i);
	}

	protected void showList1Activity()
	{
		Intent i = new Intent(this, List1Activity.class);
		startActivity(i);
	}

	protected void showAutoTextCompleteViewActivity()
	{
		Intent i = new Intent(this, AutoCompleteTextViewActivity.class);
		startActivity(i);
	}

	protected void showGridActivity()
	{
		Intent i = new Intent(this, GridActivity.class);
		startActivity(i);
	}

	protected void showSpinnerActivity2()
	{
		Intent i = new Intent(this, SpinnerActivity2.class);
		startActivity(i);

	}

	protected void showListViewActivity()
	{
		Intent i = new Intent(this, ListViewActivity.class);
		startActivity(i);
	}

	protected void showLinearLayoutActivity()
	{
		Intent i = new Intent(this, LinearLayoutActivity.class);
		startActivity(i);
	}

	protected void showImageViewActivity()
	{
		Intent i = new Intent(this, ImageViewActivity.class);
		startActivity(i);
	}

	protected void showButtonReverseActivity()
	{
		Intent i = new Intent(this, ButtonReverseActivity.class);
		startActivity(i);
	}

	protected void showOneButtonActivity()
	{
		Intent i = new Intent(this, OneButtonActivity.class);
		startActivity(i);
	}

	protected void showBuildingMenuActivity()
	{
		Intent i = new Intent(this, BuildingMenuActivity.class);
		startActivity(i);
	}

	protected void showPhysicalKeyPressActivity()
	{
		Intent i = new Intent(this, PhysicalKeyPressActivity.class);
		startActivity(i);
	}

	protected void showSeekBarActivity()
	{
		Intent i = new Intent(this, SeekBarActivity.class);
		startActivity(i);
	}

	protected void showProgressBarActivity()
	{
		Intent i = new Intent(this, ProgressBarActivity.class);
		startActivity(i);

	}

	protected void showSpinnerActivity()
	{
		Intent i = new Intent(this, SpinnerActivity.class);
		startActivity(i);
	}

	protected void showToggleButtonactivity()
	{
		Intent i = new Intent(this, ToggleButtonActivity.class);
		startActivity(i);
	}

	protected void showRadioButtonActivity()
	{
		Intent i = new Intent(this, RadioButtonActivity.class);
		startActivity(i);
	}

	protected void showCheckBoxActivity()
	{
		Intent i = new Intent(this, CheckBoxActivity.class);
		startActivity(i);
	}

	protected void showImageButtonActivity()
	{
		Intent i = new Intent(this, ImageButtonActivity.class);
		startActivity(i);
	}

	protected void showSomethingElse()
	{

	}

	protected void showChap1_4()
	{
		Intent i = new Intent(this, MenuScreenActivity.class);
		startActivity(i);
	}

}
