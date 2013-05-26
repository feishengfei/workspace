package com.cookbook.layout_widgets.list;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cookbook.launch_activity.R;

public class List3Activity extends ListActivity
{
    private ArrayList<String> items = new ArrayList<String>();
	private Button btn;
        
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        btn = (Button)findViewById(R.id.Button01);
        
    	InputStream in = getResources().openRawResource(R.raw.week);
        try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in, null);
            
            NodeList words = doc.getElementsByTagName("day");
            for (int i = 0; i < words.getLength(); i++) {
				items.add( ((Element)words.item(i)).getAttribute("value"));
			}            
            
            in.close();
            
		}
		catch (Throwable e) {
            Toast.makeText(this, "Exception:" + e.toString(), Toast.LENGTH_SHORT)
                .show();
		}
        
		setListAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, items));
    }
    
    private void setupView()
	{
		
	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	super.onListItemClick(l, v, position, id);
        Toast.makeText(this, items.get(position).toString(), Toast.LENGTH_SHORT).show();
    }
}
