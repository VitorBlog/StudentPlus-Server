package com.vitorblog.studentplusserver.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.vitorblog.studentplusserver.process.StudentProcess
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class StudentController {
    private val objectMapper = ObjectMapper()

    @GetMapping("/student/{id}")
    @ResponseBody
    fun getStudent(@PathVariable id:String): String {
        val student = StudentProcess().load(id)

        if (student == null){
            val json = objectMapper.createObjectNode()

            json.put("success", false)
            json.put("message", "Student not found.")

            return json.toString()
        }

        return objectMapper.writeValueAsString(student)
    }

    @GetMapping("/student/{id}/update")
    @ResponseBody
    fun updateStudent(@PathVariable id:String): String {
        val student = StudentProcess().loadMinimal(id)

        if (student == null){
            val json = objectMapper.createObjectNode()

            json.put("success", false)
            json.put("message", "Student not found.")

            return json.toString()
        }

        return objectMapper.writeValueAsString(student)
    }

}