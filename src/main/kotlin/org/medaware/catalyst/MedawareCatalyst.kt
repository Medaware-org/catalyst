package org.medaware.catalyst

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
class MedawareCatalyst

fun main(args: Array<String>) {
    runApplication<MedawareCatalyst>(*args)
}