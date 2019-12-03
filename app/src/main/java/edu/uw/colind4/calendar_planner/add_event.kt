package edu.uw.colind4.calendar_planner

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_event.*
import java.util.*
import android.app.TimePickerDialog
import android.util.Log
import android.view.View
import java.text.SimpleDateFormat

class add_event : AppCompatActivity() {

    var chosen_date: Int? = null
    var chosen_month: Int? = null
    var chosen_year: Int? = null
    var timeMillis: Int? = null
    var notifications: String? = "true"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

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
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                pickTimeBtn.text = SimpleDateFormat("HH:mm").format(cal.time)
                timeMillis = cal.timeInMillis.toInt()
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        toggle_btn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                pickTimeBtn.setVisibility(View.GONE)

            } else {
                // The toggle is disabled
                pickTimeBtn.setVisibility(View.VISIBLE)
            }
        }

        address_btn.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                address_input.setVisibility(View.GONE)

            } else {
                // The toggle is disabled
                address_input.setVisibility(View.VISIBLE)
            }
        }

        notification_btn.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                notifications = "true"

            } else {
                // The toggle is disabled
                notifications = "false"
            }
        }

        submit_btn.setOnClickListener {
            val dbHandler = MyDBHandler(this, null, null, 1)
            val event = Event(chosen_date!!, chosen_month!!, chosen_year!!, title_input.text.toString(), notes_input.text.toString(), address_input.text.toString(), timeMillis!!, notifications.toString())
            dbHandler.addProduct(event)
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
}
