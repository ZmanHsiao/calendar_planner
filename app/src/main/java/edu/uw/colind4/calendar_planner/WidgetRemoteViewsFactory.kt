package edu.uw.colind4.calendar_planner

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast

data class TodoItem(val name: String)

val todoList = listOf<TodoItem>(
    TodoItem("Assignment 1"),
    TodoItem("Project 2"),
    TodoItem("Exam 3")
)

class WidgetRemoteViewsFactory(private val context: Context, private val intent: Intent): RemoteViewsService.RemoteViewsFactory {
    override fun onCreate() {
        return
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
                setTextViewText(R.id.widget_list_text, todoList[position].name)
            }
    }

    override fun getCount(): Int {
        return todoList.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
        return
    }
}