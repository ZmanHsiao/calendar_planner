package edu.uw.colind4.calendar_planner

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Implementation of App Widget functionality.
 */
class HomeWidget : AppWidgetProvider() {
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show()
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateTitleOnClick(context, appWidgetManager, appWidgetId)
            updateListView(context, appWidgetManager, appWidgetId)
            updateListViewOnClick(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // Add onClick listener to the title TextView
    private fun updateTitleOnClick(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val pendingIntent = Intent(context, add_event::class.java)
            .let {
                PendingIntent.getActivity(context, 0, it, 0)
            }
        val views = RemoteViews(context.packageName, R.layout.home_widget)
            .apply {
                setOnClickPendingIntent(R.id.appwidget_title_text, pendingIntent)
            }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    // Update ListView in the widget
    private fun updateListView(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val intent = Intent(context, WidgetRemoteViewsService::class.java)
        val pendingIntent = Intent(context, MainActivity::class.java)
            .let {
                PendingIntent.getActivity(context, 1, it, 1)
            }
        val views = RemoteViews(context.packageName, R.layout.home_widget)
            .apply {
                setRemoteAdapter(R.id.appwidget_list, intent)
            }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun updateListViewOnClick(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val pendingIntent = Intent(context, MainActivity::class.java)
            .let {
                PendingIntent.getActivity(context, 0, it, 0)
            }
        val views = RemoteViews(context.packageName, R.layout.home_widget)
            .apply {
                setPendingIntentTemplate(R.id.appwidget_list, pendingIntent)
            }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
