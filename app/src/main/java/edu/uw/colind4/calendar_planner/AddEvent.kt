package edu.uw.colind4.calendar_planner

import android.app.*
import android.content.BroadcastReceiver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_event.*
import java.util.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat

class AddEvent : AppCompatActivity() {

    var chosen_date: Int? = null
    var chosen_month: Int? = null
    var chosen_year: Int? = null
    var chosen_hour: Int? = null
    var chosen_min: Int? = null
    var notifications: String? = "true"
    val CHANNEL_ID : String = "reminder_channel"
    var toggleAddress: Boolean = true
    var toggleTime: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val intentFilter= IntentFilter("notify")
        val NotificationBroadcastReceiver = MyBroadcastReceiverClass()
        registerReceiver(NotificationBroadcastReceiver, intentFilter)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)

        date_btn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                chosen_date = mDay
                chosen_month = mMonth + 1
                chosen_year = mYear
                date_btn.setText("" + (mMonth + 1) + "/" + mDay + "/"+ mYear)
            }, year, month, day)
            dpd.show()
        }

        pickTimeBtn.setOnClickListener {
            val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    chosen_hour = hour
                    chosen_min = minute
                    cal.set(Calendar.HOUR_OF_DAY, chosen_hour!!)
                    cal.set(Calendar.MINUTE, chosen_min!!)
                    pickTimeBtn.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        toggle_btn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                pickTimeBtn.setVisibility(View.GONE)
                toggleTime = false

            } else {
                // The toggle is disabled
                pickTimeBtn.setVisibility(View.VISIBLE)
                toggleTime = true
            }
        }

        address_btn.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                toggleAddress = false
                address_input.setVisibility(View.GONE)

            } else {
                // The toggle is disabled
                toggleAddress = true
                address_input.setVisibility(View.VISIBLE)
            }
        }

        notification_btn.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                notifications = "false"
                notification_input.setVisibility(View.GONE)

            } else {
                // The toggle is disabled
                notifications = "true"
                notification_input.setVisibility(View.VISIBLE)
            }
        }

        submit_btn.setOnClickListener {
            if (chosen_date == null) {
                Toast.makeText(this, "You must choose a Date", Toast.LENGTH_LONG).show()
            } else if (chosen_min == null && toggleTime == true) {
                Toast.makeText(
                    this,
                    "You must choose a time or choose \"All Day Event\"",
                    Toast.LENGTH_LONG
                ).show()
            } else if(title_input.text.isEmpty()){
                Toast.makeText(
                    this,
                    "You must include a title",
                    Toast.LENGTH_LONG
                ).show()
            }else if (address_input.text.isEmpty() && toggleAddress == true) {
                Toast.makeText(
                    this,
                    "You must choose an address or choose \"No Specific Address\"",
                    Toast.LENGTH_LONG
                ).show()
            } else if (notifications.equals("true") && chosen_min == null) {
                Toast.makeText(
                    this,
                    "You must choose a Time if you want to set a reminder",
                    Toast.LENGTH_LONG
                ).show()
            } else if(notifications.equals("true") && toggleTime == false) {
                Toast.makeText(
                    this,
                    "You must choose a Time if you want to set a reminder",
                    Toast.LENGTH_LONG
                ).show()
            }else {
                val dbHandler = MyDBHandler(this, null, null, 1)

                var final_address: String? = ""
                if(toggleAddress == true) {
                    final_address = address_input.text.toString()
                }

                var timeInMilli: Long? = null

                if(toggleTime == true) {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, chosen_hour!!)
                    cal.set(Calendar.MINUTE, chosen_min!!)
                    cal.set(Calendar.DATE, chosen_date as Int)
                    cal.set(Calendar.MONTH, chosen_month!!.minus(1) as Int)
                    cal.set(Calendar.YEAR, chosen_year as Int)
                    timeInMilli = cal.timeInMillis - Calendar.getInstance().timeInMillis
                }

                val event = Event(chosen_date!!, chosen_month!!, chosen_year!!, title_input.text.toString(), notes_input.text.toString(), final_address, timeInMilli?.toInt(), notifications.toString())
                dbHandler.addProduct(event)

                if(notifications.equals("true")) {
                    if(!notification_input.text.isEmpty()) {
                        timeInMilli = timeInMilli?.minus(notification_input.text.toString().toLong() * 1000 * 60)
                    }
                    scheduleNotification(this, timeInMilli!!, 1)
                }
            }
        }

        test_btn.setOnClickListener {
            val dbHandler = MyDBHandler(this, null, null, 1)
            var result = dbHandler.findEventsList("25", "12", "2019")
            test_view.text = result?.size.toString()
            //test_view2.text = "month = ${result!![5].month}, day = ${result!![4].day} year = ${result!![4].year} "
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "reminder"
            val descriptionText = "reminder notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotification(context: Context, delay: Long, notificationId: Int) {

        val intent = Intent(context, MainActivity::class.java)
        val pIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell_icon)
            .setContentTitle(title_input.text.toString())
            .setContentText(notes_input.text.toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pIntent)
            .setAutoCancel(true)

        val notification = builder.build()

        val notificationIntent = Intent("notify")
        notificationIntent.putExtra(MyBroadcastReceiverClass.Constants.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(MyBroadcastReceiverClass.Constants.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("TIM", delay.toString())
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent)
    }

    class MyBroadcastReceiverClass(): BroadcastReceiver() {
        var notificationID: Int = -1

        object Constants {
            const val NOTIFICATION_ID: String = "notification_id"
            const val NOTIFICATION: String = "notification"
            }

        override fun onReceive(c: Context, intent: Intent) {
            if (intent.getAction()!!.equals("notify")) {
                val notificationManager = c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification = intent.getParcelableExtra<Notification>(Constants.NOTIFICATION)
                val notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, getUniqueID())
                notificationManager.notify(notificationId, notification)
            }
        }

        fun getUniqueID(): Int {
            notificationID = notificationID + 1
            return notificationID
        }
    }
}
