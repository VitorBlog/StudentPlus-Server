package com.vitorblog.studentplusserver.process

import com.fasterxml.jackson.databind.ObjectMapper
import com.vitorblog.studentplusserver.model.*
import com.vitorblog.studentplusserver.requester.APIRequester
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.text.SimpleDateFormat

class MenuProcess {
    private val jsonParser = JSONParser()

    fun load(id:String):ArrayList<MenuItem> {
        var menuList = arrayListOf<MenuItem>()

        val menuResponse = APIRequester.request(Point.GET_SCHOOL_MENU, "{\"AlunoCod\": $id}")
        val menuJSON = jsonParser.parseArray(jsonParser.parseJSON(menuResponse).getString("ResultadoCardapio"))

        for (me in menuJSON){
            val menu = me as JSONObject

            menuList.add(MenuItem(menu.getString("DataCardapio"), (menu.getArray("CardapiosDoDia")[0] as JSONObject).getString("DescricaoCardapio")))
        }

        return menuList
    }

}