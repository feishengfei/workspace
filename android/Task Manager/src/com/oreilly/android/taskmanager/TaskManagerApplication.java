package com.oreilly.android.taskmanager;

import java.util.ArrayList;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.oreilly.android.taskmanager.tasks.Task;
import com.oreilly.android.taskmanager.tasks.TasksSQLiteOpenHelper;

import static com.oreilly.android.taskmanager.tasks.TasksSQLiteOpenHelper.*;

public class TaskManagerApplication extends Application
{
	private ArrayList<Task> currentTasks;
	private SQLiteDatabase database;

	@Override
	public void onCreate()
	{
		super.onCreate();
        TasksSQLiteOpenHelper  helper = new TasksSQLiteOpenHelper(this);
        database = helper.getWritableDatabase();
        if(null == currentTasks) {
            loadTasks();
        }
	}

	private void loadTasks()
	{
		currentTasks = new ArrayList<Task>();
		Cursor tasksCursor = database.query(
			TASKS_TABLE, 
			new String[]{ TASK_ID, TASK_NAME, TASK_COMPLETE }, 
			null, null, null, null, 
			String.format("%s, %s", TASK_COMPLETE, TASK_NAME)
		);
        tasksCursor.moveToFirst();
        Task t;
        if (! tasksCursor.isAfterLast()) {
            do {
                long id = tasksCursor.getLong(0);
                String name = tasksCursor.getString(1);
                String boolValue = tasksCursor.getString(2);
                boolean complete = Boolean.parseBoolean(boolValue);
                t = new Task(name);
                t.setComplete(complete);
                t.setId(id);
                currentTasks.add(t);
            } while (tasksCursor.moveToNext());
        }
        tasksCursor.close();
	}

	public ArrayList<Task> getCurrentTasks()
	{
		return currentTasks;
	}

	public void setCurrentTasks(ArrayList<Task> currentTasks)
	{
		this.currentTasks = currentTasks;
	}
    
	public void addTask(Task t)
	{
	    assert(null != t);
        ContentValues values = new ContentValues();
        values.put(TASK_NAME, t.getName());
        values.put(TASK_COMPLETE, Boolean.toString(t.isComplete()));
        t.setId(database.insert(TASKS_TABLE, null, values));
        currentTasks.add(t);
	}
    
	public void saveTask(Task t) 
	{
        assert(null != t);
        
        ContentValues values = new ContentValues();
        values.put(TASK_NAME, t.getName());
        values.put(TASK_COMPLETE, Boolean.toString(t.isComplete()));
        
        long id = t.getId();
        String where = String.format("%s = %s", TASK_ID, id);
        
        database.update(TASKS_TABLE, values, where, null);
	}
    
	public void deleteTasks(Long[] ids) 
	{
        StringBuffer idList = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            idList.append(ids[i]);
            if (i < ids.length - 1) {
                idList.append(",");
            }
        }
        System.out.println(idList);
        
        String where = String.format("%s in (%s)", TASK_ID, idList);
        database.delete(TASKS_TABLE, where, null);
	}
    
}