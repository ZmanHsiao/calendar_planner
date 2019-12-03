package edu.uw.colind4.calendar_planner

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_map)

        updateWidget()
    }

    private fun updateWidget() {
        val intent = Intent(this, HomeWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(this).getAppWidgetIds(ComponentName(this, AppWidgetManager::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ids)
        sendBroadcast(intent);
    }
}
