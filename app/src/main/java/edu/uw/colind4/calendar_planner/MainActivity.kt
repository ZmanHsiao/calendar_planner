package edu.uw.colind4.calendar_planner

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addAddButton()
        updateWidget()
    }

    private fun updateWidget() {
        val intent = Intent(this, HomeWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(this).getAppWidgetIds(ComponentName(this, AppWidgetManager::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ids)
        sendBroadcast(intent);
    }

    private fun addAddButton() {
        button.setOnClickListener {
            val intent = Intent(this, add_event::class.java)
            this.startActivity(intent)
        }
    }
}
