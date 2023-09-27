package com.metawebthree.keykeeper.service;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

//  自定义桌面小组件
public class DoAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}

class UIRunnable implements Runnable {

    private Context context;

    @Override
    public void run() {
        RemoteViews views = new RemoteViews(context.getPackageName(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//        views.setTextViewText(R.id.appwidget_text_sub, widgetText);
    }
}