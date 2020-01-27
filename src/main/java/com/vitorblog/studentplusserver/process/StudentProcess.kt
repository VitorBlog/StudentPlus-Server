package com.vitorblog.studentplusserver.process

import com.fasterxml.jackson.databind.ObjectMapper
import com.vitorblog.studentplusserver.model.*
import com.vitorblog.studentplusserver.requester.APIRequester
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.text.SimpleDateFormat

class StudentProcess {
    private val jsonParser = JSONParser()
    private val subjectProcess = SubjectProcess()
    private val reportProcess = ReportProcess()
    private val scheduleProcess = ScheduleProcess()
    private val menuProcess = MenuProcess()

    fun load(id:String):Student?{
        val studentInfoResponse = APIRequester.request(Point.GET_STUDENT_INFO, "{\"AlunoCod\": $id}")
        if (studentInfoResponse.equals("{\"ResultadoAluno\":\"[]\"}")) { return null }
        val studentInfoJSON = jsonParser.parseArray(jsonParser.parseJSON(studentInfoResponse).getString("ResultadoAluno")).first() as JSONObject
        
        val birthday = studentInfoJSON.getString("AlunoDataNasc")
        val subjects = subjectProcess.load(id, birthday)
        val report = reportProcess.load(id)
        val schedule = scheduleProcess.load(id)
        val menu = menuProcess.load(id)
        val school = School(studentInfoJSON.getString("UeCod"), studentInfoJSON.getString("UeNom").formatName().replace("eeb", "EEB", true), School.Location(studentInfoJSON.getString("UeMunNom"), "${studentInfoJSON.getString("UeEnd").formatName()}, ${studentInfoJSON.getString("UeNumEnd")}", studentInfoJSON.getString("UeLatitude"), studentInfoJSON.getString("UeLongitude")), schedule, menu)

        return Student(id, studentInfoJSON.getString("AlunoNom").formatName(), birthday, subjects, report, school)
    }

    fun loadMinimal(id:String):StudentMini?{
        val studentInfoResponse = APIRequester.request(Point.GET_STUDENT_INFO, "{\"AlunoCod\": $id}")
        if (studentInfoResponse.equals("{\"ResultadoAluno\":\"[]\"}")) { return null }
        val studentInfoJSON = jsonParser.parseArray(jsonParser.parseJSON(studentInfoResponse).getString("ResultadoAluno")).first() as JSONObject

        val subjects = subjectProcess.load(id, studentInfoJSON.getString("AlunoDataNasc"))
        val report = reportProcess.load(id)

        return StudentMini(id, subjects, report)
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
