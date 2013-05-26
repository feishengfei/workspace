package com.cookbook.simple_widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class SimpleWidgetActivity extends AppWidgetProvider {
    private final static int APPWIDGET = 1001;

	@Override
	public void onUpdate(Context context, 
			AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
        
		final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
            String titlePrefix = "Time since the widget was started:" ;
            updateAppWidget(context, 
            		appWidgetManager, appWidgetId, titlePrefix);
		}
	}

	private void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			String titlePrefix)
	{
        Long millis = System.currentTimeMillis();
        int seconds = (int)(millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        
        String text = titlePrefix;
        text += " " + minutes + ":" + String.format("%02d", seconds);
        
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.widget_example_text, text);
		
        appWidgetManager.updateAppWidget(appWidgetId, views);
	}
    
    
}