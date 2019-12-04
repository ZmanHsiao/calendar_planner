package edu.uw.colind4.calendar_planner

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_day_schedule.*
import kotlinx.android.synthetic.main.rview_layout.view.*


class DaySchedule : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_schedule)
        rview.layoutManager = LinearLayoutManager(this)
        rview.adapter = MyRecyclerViewAdapter(this, arrayListOf("asdfasdf"))
    }
}

class MyRecyclerViewAdapter(context: Context, data: List<String>): RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    var inflater = LayoutInflater.from(context)
    var mData = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = inflater.inflate(R.layout.rview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewAdapter.ViewHolder, pos: Int) {
        holder.tView.text = mData.get(pos)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tView = view.event
    }
}