package edu.uw.colind4.calendar_planner

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_day_schedule.*
import kotlinx.android.synthetic.main.rview_layout.view.*


class DaySchedule : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_schedule)
        var day = intent.extras!!.getInt("day").toString()
        var month = intent.extras!!.getInt("month").toString()
        var year = intent.extras!!.getInt("year").toString()
        date.text = "$month/$day/$year"
        var db = MyDBHandler(this, null, null, 1)
        var events = db.findEventsList(day, month, year)
        rview.layoutManager = LinearLayoutManager(this)
        rview.adapter = MyRecyclerViewAdapter(this, events!!)
    }
}

class MyRecyclerViewAdapter(context: Context, data: List<Event>): RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    var inflater = LayoutInflater.from(context)
    var mData = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = inflater.inflate(R.layout.rview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewAdapter.ViewHolder, pos: Int) {
        holder.address.text = mData.get(pos).address
        holder.notes.text = mData.get(pos).notes
        holder.title.text = mData.get(pos).title
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title = view.title
        val address = view.address
        val notes = view.notes
    }
}