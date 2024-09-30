package org.medaware.catalyst

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CatalystApplication

fun main(args: Array<String>) {
	runApplication<CatalystApplication>(*args)
}
