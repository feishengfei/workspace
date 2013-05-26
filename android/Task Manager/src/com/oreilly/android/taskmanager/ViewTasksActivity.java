package com.oreilly.android.taskmanager;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.oreilly.android.taskmanager.adapter.TaskListAdapter;
import com.oreilly.android.taskmanager.tasks.Task;

public class ViewTasksActivity extends ListActivity {
    
    private Button addButton;
	private TaskManagerApplication app;
	private TaskListAdapter adapter;
	private Button removeButton;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpViews(); 
    
        app = (TaskManagerApplication)getApplication();
        adapter = new TaskListAdapter(app.getCurrentTasks(), this);
        setListAdapter(adapter);
    }
    
	@Override
	protected void onResume()
	{
		super.onResume();
        if (null != app.getCurrentTasks()) {
            ArrayList<Task>	tasks = app.getCurrentTasks();
            for (Task task : tasks) {
                System.out.println("tasks:" + task);				
			}
        }
        if (null != adapter) {
            adapter.forceReload();	
        }
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
        adapter.toggleTaskCompleteAtPosition(position);
        Task t = adapter.getItem(position);
        app.saveTask(t); 
	}
    
	protected void removeCompleteTasks()
	{
	    Long[] ids = adapter.removeCompletedTasks();	
        app.deleteTasks(ids);
	}

	private void setUpViews()
	{
	    addButton = (Button)findViewById(R.id.add_button);
        removeButton = (Button)findViewById(R.id.remove_button);
        addButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                Intent intent = new Intent(ViewTasksActivity.this, AddTaskActivity.class);
			    startActivity(intent);
			}
		});
        removeButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
                removeCompleteTasks();
			}
		});
	}

    
}