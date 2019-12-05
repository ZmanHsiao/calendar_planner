package edu.uw.colind4.calendar_planner

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import java.util.*

class WidgetRemoteViewsFactory(private val context: Context, private val intent: Intent): RemoteViewsService.RemoteViewsFactory {
    private var dbHandler: MyDBHandler? = null
    private var result : List<Event>? = null

    override fun onCreate() {
        dbHandler = MyDBHandler(context, null, null, 1)
        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE).toString()
        val month = calendar.get(Calendar.MONTH).plus(1).toString()
        val year = calendar.get(Calendar.YEAR).toString()
        result = dbHandler?.findEventsList(date, month, year)
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onDataSetChanged() {
        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.DATE).toString()
        val month = calendar.get(Calendar.MONTH).plus(1).toString()
        val year = calendar.get(Calendar.YEAR).toString()
        result = dbHandler?.findEventsList(date, month, year)
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getViewAt(position: Int): RemoteViews {
        val event = result?.get(position) as Event
        val day = event.day
        val month = event.month
        val year = event.year
        val fillInIntent = Intent()
            .apply {
                putExtra("day", day)
                putExtra("month", month)
                putExtra("year", year)
            }
        return RemoteViews(context.packageName, R.layout.widget_list_item)
            .apply {
                setTextViewText(R.id.widget_list_text, event.title)
                setOnClickFillInIntent(R.id.widget_list_item, fillInIntent)
            }
    }

    override fun getCount(): Int {
        return result?.size ?: 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }
}