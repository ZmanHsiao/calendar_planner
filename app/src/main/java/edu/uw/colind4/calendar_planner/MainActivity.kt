package edu.uw.colind4.calendar_planner


import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateWidget()

        //check if we have access to users location
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }



        var sdf = SimpleDateFormat("dd/MM/yyyy")
        val netdate = Date(calendar.date)
        var time = sdf.format(netdate)
        var tarray = time.split("/")
        var sday = tarray[0]
        var smonth = tarray[1]
        var syear = tarray[2]
        open.setOnClickListener{openDay(tarray[0].toInt(), tarray[1].toInt(), tarray[2].toInt())}
        calendar.setOnDateChangeListener( object: CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(
                view: CalendarView,
                year: Int,
                month: Int,
                dayOfMonth: Int
            ) {
                sday = dayOfMonth.toString()
                smonth = (month + 1).toString()
                syear = year.toString()
                open.setOnClickListener{openDay(dayOfMonth, month + 1, year)}
            }
        })
        newEventBtn.setOnClickListener {
            val intent = Intent(this, AddEvent::class.java)
            intent.putExtra("day", sday)
            intent.putExtra("month", smonth)
            intent.putExtra("year", syear)
            this.startActivity(intent)
        }

    }

    private fun openDay(day: Int, month: Int, year: Int) {
        val intent = Intent(this, DaySchedule::class.java)
        intent.putExtra("day", day)
        intent.putExtra("month", month)
        intent.putExtra("year", year)
        startActivity(intent)
    }

    private fun updateWidget() {
        val intent = Intent(this, HomeWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(this)
            .getAppWidgetIds(ComponentName(this, AppWidgetManager::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ids)
        sendBroadcast(intent)
    }

    private fun addAddButton() {
        newEventBtn.setOnClickListener {
            val intent = Intent(this, AddEvent::class.java)
            this.startActivity(intent)
        }
    }
}