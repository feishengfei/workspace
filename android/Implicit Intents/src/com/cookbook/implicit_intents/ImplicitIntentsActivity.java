package com.cookbook.implicit_intents;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ImplicitIntentsActivity extends ListActivity {
    
    private static final String[] ACTIVITY_CHOICES = new String[] {
        "Open Website Example",
        "Open Contacts",
        "Open Phone Dialer Example",
        "Search Google Example",
        "Start Voice Command",
    };
    private final String searchTerms = "superman";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, 
        		android.R.layout.simple_list_item_1, ACTIVITY_CHOICES));
        setupview();
    }

	private void setupview()
	{
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setTextFilterEnabled(true);
        getListView().setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, 
					int arg2, long arg3)
			{
                switch(arg2) {
                	case 0:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.android.com/")));
                		break;
                	case 1:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));
                		break;
                	case 2:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:15001399705")));
                		break;
                	case 3:
                        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                        intent.putExtra(SearchManager.QUERY, searchTerms);
                        startActivity(intent);
                		break;
                	case 4:
                        startActivity(new Intent(Intent.ACTION_VOICE_COMMAND));
                		break;
                    default:
                    	break;
                }
				
			}
		});
		
	}
}