package org.medaware.catalyst

import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["org.medaware.catalyst", "org.medaware.catalyst.api", "org.medaware.catalyst.dto"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
