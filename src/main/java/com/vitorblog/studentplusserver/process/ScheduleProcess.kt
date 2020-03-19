package com.vitorblog.studentplusserver.process

import com.fasterxml.jackson.databind.ObjectMapper
import com.vitorblog.studentplusserver.model.*
import com.vitorblog.studentplusserver.requester.APIRequester
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.text.SimpleDateFormat

class ScheduleProcess {
    private val jsonParser = JSONParser()

    fun load(id:String):ArrayList<ScheduleItem> {
        var scheduleList = arrayListOf<ScheduleItem>()

        val scheduleResponse = APIRequester.request(Point.GET_SCHOOL_SCHEDULE, "{\"AlunoCod\": $id}")
        val scheduleJSON = jsonParser.parseArray(jsonParser.parseJSON(scheduleResponse).getString("ResultadoAgenda")).first() as JSONObject
        if (!scheduleJSON.containsKey("AgendaAtividade")){ return scheduleList }

        for (sch in scheduleJSON.getArray("AgendaAtividade")){
            val schedule = sch as JSONObject

            scheduleList.add(ScheduleItem(schedule.getString("AtividadeData"), schedule.getString("AtividadeDescricao"), schedule.getString("AtividadeObservacao")))
        }

        return scheduleList
    }

}