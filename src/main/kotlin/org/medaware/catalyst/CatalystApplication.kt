package org.medaware.catalyst

import org.medaware.catalyst.api.ArticleApiController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CatalystApplication

fun main(args: Array<String>) {
	val a: ArticleApiController? = null
	runApplication<CatalystApplication>(*args)
}
