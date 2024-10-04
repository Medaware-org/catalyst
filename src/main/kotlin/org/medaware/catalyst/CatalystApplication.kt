package org.medaware.catalyst

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
class CatalystApplication

fun main(args: Array<String>) {
    runApplication<CatalystApplication>(*args)
}
