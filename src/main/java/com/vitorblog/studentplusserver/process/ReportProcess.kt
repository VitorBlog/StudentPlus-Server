package com.vitorblog.studentplusserver.process

import com.fasterxml.jackson.databind.ObjectMapper
import com.vitorblog.studentplusserver.model.*
import com.vitorblog.studentplusserver.requester.APIRequester
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.text.SimpleDateFormat

class ReportProcess {
    val jsonParser = JSONParser()

    fun load(id:String):Report {
        var first = arrayListOf<ReportGrade>()
        var second = arrayListOf<ReportGrade>()
        var third = arrayListOf<ReportGrade>()

        val reportResponse = APIRequester.request(Point.GET_STUDENT_REPORT, "{\"AlunoCod\": $id}")
        val reportJSON = jsonParser.parseArray(jsonParser.parseJSON(reportResponse).getString("ResultadoBoletim")).first() as JSONObject

        for (sub in reportJSON.getArray("NotaFaltaDisciplina")){
            val subject = sub as JSONObject
            val name = subject.getString("DisciplinaNome").formatName()

            for (i in 1..3){
                val grade = subject.getLong("Nota$i").toDouble()
                val absence = subject.getLong("Falta$i").toInt()

                when (i) {
                    1 -> first.add(ReportGrade(name, grade, absence))
                    2 -> second.add(ReportGrade(name,grade, absence))
                    3 -> third.add(ReportGrade(name, grade, absence))
                }
            }
        }

        return Report(first, second, third)
    }

    fun String.formatName():String {
        val split = this.split(" ")
        var fullName = ""

        for (name in split){
            fullName += " ${name.substring(0, 1).toUpperCase()}${name.substring(1).toLowerCase()}"
        }

        return fullName.substring(1)
    }

}
