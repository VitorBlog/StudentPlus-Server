package com.vitorblog.studentplusserver.model

class School(val id:String, val name:String, val location:Location, val schedule:ArrayList<ScheduleItem>, val menu:ArrayList<MenuItem>) {
    class Location(val city:String, val address:String, val latitude:String, val longitude:String)
}