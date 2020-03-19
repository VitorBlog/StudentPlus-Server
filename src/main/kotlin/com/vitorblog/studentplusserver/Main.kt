package com.vitorblog.studentplusserver

import com.vitorblog.studentplusserver.process.ConfigProcess
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            ConfigProcess().load()

            SpringApplication.run(Main::class.java, "")
        }

    }

}