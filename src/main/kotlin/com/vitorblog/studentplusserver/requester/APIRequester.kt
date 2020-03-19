package com.vitorblog.studentplusserver.requester

import com.vitorblog.studentplusserver.model.Point
import java.net.HttpURLConnection
import java.net.URL

object APIRequester {

    fun request(point:Point, data:String):String{
        val url = URL(point.url)
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Content-type", "application/json")
        connection.requestMethod = "POST"
        connection.doOutput = true

        connection.outputStream.write(data.toByteArray())
        connection.outputStream.flush()
        connection.outputStream.close()

        return connection.inputStream.reader().readText()
    }

}