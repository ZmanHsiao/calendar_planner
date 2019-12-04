package edu.uw.colind4.calendar_planner

class Event {
    var id: Int?
    var day: Int?
    var month: Int?
    var year: Int?
    var title: String
    var notes: String?
    var address: String?
    var time: Int?
    var reminder: String?

    constructor(dID: Int, dDay: Int?, dmonth: Int?, dyear: Int?, dtitle: String, dnotes: String?, daddress: String?, dtime: Int?, dreminder: String?) {
        this.id = dID
        this.day = dDay
        this.month = dmonth
        this.year = dyear
        this.title = dtitle
        this.notes = dnotes
        this.address = daddress
        this.time = dtime
        this.reminder = dreminder
    }

    constructor(dDay: Int?, dmonth: Int, dyear: Int?, dtitle: String, dnotes: String?, daddress: String?, dtime: Int?, dreminder: String?) {
        this.id = null
        this.day = dDay
        this.month = dmonth
        this.year = dyear
        this.title = dtitle
        this.notes = dnotes
        this.address = daddress
        this.time = dtime
        this.reminder = dreminder
    }
}