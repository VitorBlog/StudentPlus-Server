package com.vitorblog.studentplusserver.process

import com.vitorblog.studentplusserver.model.Config
import org.json.simple.fork.parser.JSONParser
import java.io.File

class ConfigProcess {
    private val jsonParser = JSONParser()

    fun load(){
        val jsonObject = jsonParser.parseJSON(File("config.json").inputStream().reader().readText())

        Config.API_KEY.string = jsonObject.getString("apiKey")
    }

}