package edu.uw.colind4.calendar_planner

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class HomeWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            val titleOnClickPendingIntent = Intent(context, AddEvent::class.java)
                .let {
                    PendingIntent.getActivity(context, 0, it, 0)
                }
            val listViewIntent = Intent(context, WidgetRemoteViewsService::class.java)
            val listViewOnClickIntent = Intent(context, DaySchedule::class.java)
                .let {
                    PendingIntent.getActivity(context, 0, it, 0)
                }
            val views = RemoteViews(context.packageName, R.layout.home_widget)
                .apply {
                    setOnClickPendingIntent(R.id.appwidget_title_text, titleOnClickPendingIntent)
                    setRemoteAdapter(R.id.appwidget_list, listViewIntent)
                    setPendingIntentTemplate(R.id.appwidget_list, listViewOnClickIntent)
                }
            appWidgetManager.updateAppWidget(appWidgetId, views)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_list)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}
