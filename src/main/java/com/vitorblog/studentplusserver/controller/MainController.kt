package com.vitorblog.studentplusserver.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class MainController {

    @GetMapping("/")
    @ResponseBody
    fun index(): String {
        return "<script>location.href='https://github.com/VitorBlog/Estudante-SC-Doc/blob/master/index.md'</script>"
    }
}