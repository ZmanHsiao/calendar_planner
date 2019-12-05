package edu.uw.colind4.calendar_planner

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_day_schedule.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.rview_layout.view.*
import java.text.SimpleDateFormat
import java.util.*


class DaySchedule : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_schedule)
        var day = intent.extras!!.getInt("day").toString()
        var month = intent.extras!!.getInt("month").toString()
        var year = intent.extras!!.getInt("year").toString()
        date.text = "$month/$day/$year"
        date.setOnClickListener{
            val intent = Intent(this, AddEvent::class.java)
            intent.putExtra("type", "add")
            this.startActivity(intent)
        }
        var db = MyDBHandler(this, null, null, 1)
        var events = db.findEventsList(day, month, year)
        rview.layoutManager = LinearLayoutManager(this)
        rview.adapter = MyRecyclerViewAdapter(this, events!!)
    }
}

class MyRecyclerViewAdapter(context: Context, data: List<Event>): RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    var inflater = LayoutInflater.from(context)
    var mData = data
    var ctx = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = inflater.inflate(R.layout.rview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewAdapter.ViewHolder, pos: Int) {
        var day = mData.get(pos).day.toString()
        var month = mData.get(pos).month.toString()
        var year = mData.get(pos).year.toString()
        holder.address.text = mData.get(pos).address
        holder.notes.text = mData.get(pos).notes
        if (mData.get(pos).time == null) {
            holder.title.text = mData.get(pos).title
        } else {
            var sdf = SimpleDateFormat("hh:mm a")
            var netdate = Date(mData.get(pos).time!!.toLong())
            var time = sdf.format(netdate)
            holder.title.text = time + " - " + mData.get(pos).title
        }


        holder.delete.setOnClickListener{
            var db = MyDBHandler(ctx, null, null, 1)
            db.deleteEvent(mData.get(pos).id!!)
            mData = db.findEventsList(day, month, year)!!
            notifyDataSetChanged()
            Toast.makeText(ctx, "Deleted", Toast.LENGTH_LONG).show()
        }
        holder.edit.setOnClickListener{
            val intent = Intent(ctx, AddEvent::class.java)
            intent.putExtra("type", "update")
            intent.putExtra("day", day)
            intent.putExtra("month", month)
            intent.putExtra("year", year)
            intent.putExtra("title", mData.get(pos).title)
            intent.putExtra("address", mData.get(pos).address)
            intent.putExtra("notes", mData.get(pos).notes)
            intent.putExtra("time", mData.get(pos).time)
            intent.putExtra("reminder", mData.get(pos).reminder)
            intent.putExtra("id", mData.get(pos).id)
            ctx.startActivity(intent)
        }
        holder.find.setOnClickListener{
            val intent = Intent(ctx, map_activity::class.java)
            intent.putExtra("address", mData.get(pos).address)
            ctx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title = view.title
        val address = view.address
        val notes = view.notes
        val delete = view.delete
        val find = view.find
        val edit = view.edit
    }
}
