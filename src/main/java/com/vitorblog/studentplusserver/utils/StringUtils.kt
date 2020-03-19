package com.vitorblog.studentplusserver.utils

object StringUtils {

    fun formatName(string:String):String {
        val split = string.split(" ")
        var fullName = ""

        for (name in split){
            if (name.isNotEmpty()){
                fullName += " ${name.substring(0, 1).toUpperCase()}${name.substring(1).toLowerCase()}"
            }
        }

        return fullName.substring(1).split("  ")[0]
    }

}