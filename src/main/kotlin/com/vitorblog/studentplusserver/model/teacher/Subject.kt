package com.vitorblog.studentplusserver.model

import kotlin.collections.ArrayList

class Subject(val id:String, val name:String, val teacher:String, val grades:ArrayList<Grade>, val messages:ArrayList<Message>) {

    class Message(val id:String, val message:String)

}