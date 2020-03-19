package com.vitorblog.studentplusserver.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView

@Controller
class MainController {

    @GetMapping("/")
    fun indexController(attributes: RedirectAttributes): RedirectView? {
        return RedirectView("https://github.com/VitorBlog/StudentPlus-Server")
    }
}