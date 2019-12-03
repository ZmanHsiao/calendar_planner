package edu.uw.colind4.calendar_planner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newEventBtn.setOnClickListener {
            val intent = Intent(this, add_event::class.java)
            this.startActivity(intent)
        }
    }
}
