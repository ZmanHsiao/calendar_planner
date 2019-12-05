package edu.uw.colind4.calendar_planner

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import android.util.Log

class MyDBHandler(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_EVENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DAY + " INTEGER,"
                + COLUMN_MONTH + " INTEGER,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_NOTES + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_TIME + " INTEGER,"
                + COLUMN_REMINDER + " TEXT"
                + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS)
        onCreate(db)
    }

    fun addProduct(event: Event) {

        val values = ContentValues()
        values.put(COLUMN_DAY, event.day)
        values.put(COLUMN_MONTH, event.month)
        values.put(COLUMN_YEAR, event.year)
        values.put(COLUMN_TITLE, event.title)
        values.put(COLUMN_NOTES, event.notes)
        values.put(COLUMN_ADDRESS, event.address)
        values.put(COLUMN_TIME, event.time)
        values.put(COLUMN_REMINDER, event.reminder)

        val db = this.writableDatabase


        db.insert(TABLE_EVENTS, null, values)
        db.close()
    }

    fun findEvent(id: Int): Event? {
        val query =
            "SELECT * FROM $TABLE_EVENTS WHERE $COLUMN_ID = \"$id\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        var result: Event? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            val id = Integer.parseInt(cursor.getString(0))
            val day = Integer.parseInt(cursor.getString(1))
            val month = Integer.parseInt(cursor.getString(2))
            val year = Integer.parseInt(cursor.getString(3))
            val title = cursor.getString(4)
            val notes = cursor.getString(5)
            val address = cursor.getString(6)
            val time = Integer.parseInt(cursor.getString(7)).toLong()
            val notification = cursor.getString(8)
            result = Event(id, day, month, year, title, notes, address, time, notification)
            cursor.close()
        }
        return result
    }

    fun findEventsList(day: String, month: String, year: String): List<Event>? {
        val query =
            "SELECT * FROM $TABLE_EVENTS WHERE $COLUMN_DAY = $day AND $COLUMN_MONTH = $month AND $COLUMN_YEAR = $year"
        //val query = "SELECT * FROM $TABLE_EVENTS"


        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)
        //cursor!!.moveToFirst()
        var events: MutableList<Event> = mutableListOf()

        while (cursor.moveToNext()) {
            val id = Integer.parseInt(cursor.getString(0))
            val day = Integer.parseInt(cursor.getString(1))
            val month = Integer.parseInt(cursor.getString(2))
            var year = Integer.parseInt(cursor.getString(3))
            val title = cursor.getString(4)
            val notes = cursor.getString(5)
            val address = cursor.getString(6)
            var time: Long? = null
            if(!cursor.isNull(7)) {
                time = cursor.getString(7).toLong()
            }
            val notification = cursor.getString(8)
            events.add(Event(id, day, month, year, title, notes, address, time, notification))
        }
        cursor.close()
        db.close()

        return events
    }

    fun createTable() {

        val db = this.writableDatabase
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_EVENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DAY + " INTEGER,"
                + COLUMN_MONTH + " INTEGER,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_NOTES + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_TIME + " INTEGER,"
                + COLUMN_REMINDER + " TEXT"
                + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
        db.close()
    }

    fun deleteEvent(id: Int): Boolean {

        var result = false

        val query =
            "SELECT * FROM $TABLE_EVENTS WHERE $COLUMN_ID = \"$id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_EVENTS, COLUMN_ID + " = ${id.toString()}", null)
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "eventDB.db"
        val TABLE_EVENTS = "events"

        val COLUMN_ID = "id"
        val COLUMN_DAY = "day"
        val COLUMN_MONTH = "month"
        val COLUMN_YEAR = "year"
        val COLUMN_TITLE = "title"
        val COLUMN_NOTES = "notes"
        val COLUMN_ADDRESS = "address"
        val COLUMN_TIME = "time"
        val COLUMN_REMINDER = "reminder"

    }
}