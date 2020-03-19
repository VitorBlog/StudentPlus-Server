package com.vitorblog.studentplusserver.process

import com.fasterxml.jackson.databind.ObjectMapper
import com.vitorblog.studentplusserver.model.*
import com.vitorblog.studentplusserver.requester.APIRequester
import com.vitorblog.studentplusserver.utils.StringUtils
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

            for (i in 1..3){
                when (i) {
                    1 -> first.add(ReportGrade(StringUtils.formatName(subject.getString("DisciplinaNome")), subject.getLong("Nota$i").toDouble(), subject.getLong("Falta$i").toInt()))
                    2 -> second.add(ReportGrade(StringUtils.formatName(subject.getString("DisciplinaNome")), subject.getLong("Nota$i").toDouble(), subject.getLong("Falta$i").toInt()))
                    3 -> third.add(ReportGrade(StringUtils.formatName(subject.getString("DisciplinaNome")), subject.getLong("Nota$i").toDouble(), subject.getLong("Falta$i").toInt()))
                }
            }
        }

        return Report(first, second, third)
    }

}