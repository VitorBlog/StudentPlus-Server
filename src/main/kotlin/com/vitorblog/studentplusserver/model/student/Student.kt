package com.vitorblog.studentplusserver.model

import java.util.*
import kotlin.collections.ArrayList

class Student(val id:String, val name:String, val birthdate:String, val subjects:ArrayList<Subject>, val report:Report, val school:School, val studentID:StudentID)