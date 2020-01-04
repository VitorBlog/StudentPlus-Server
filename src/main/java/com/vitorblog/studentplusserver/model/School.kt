package com.vitorblog.studentplusserver.model

class School(val id:String, val name:String, val location:Location) {
    class Location(val city:String, val address:String, val latitude:String, val longitude:String)
}