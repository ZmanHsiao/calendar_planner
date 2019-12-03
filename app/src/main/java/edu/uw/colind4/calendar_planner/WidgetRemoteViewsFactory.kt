package edu.uw.colind4.calendar_planner

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService

data class TodoItem(val name: String, val date: String)

class WidgetRemoteViewsFactory(private val context: Context, private val intent: Intent): RemoteViewsService.RemoteViewsFactory {
    private var dbHandler: MyDBHandler? = null
    private var result : List<Event>? = null

    override fun onCreate() {
        dbHandler = MyDBHandler(context, null, null, 1)
        result = dbHandler?.findEventsList("25", "12", "2019")
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onDataSetChanged() {
        return
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_list_item)
            .apply {
                // TODO: Add OnClick listener to each ListView item
                setTextViewText(R.id.widget_list_text, result?.get(position)?.title.toString())
            }
    }

    override fun getCount(): Int {
        return result?.size ?: 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
        return
    }
}