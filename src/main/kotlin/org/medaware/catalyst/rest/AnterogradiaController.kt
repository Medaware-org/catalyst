package org.medaware.catalyst.rest

import org.medaware.anterogradia.Anterogradia
import org.medaware.anterogradia.rootCause
import org.medaware.catalyst.api.AnterogradiaApi
import org.medaware.catalyst.service.AnterogradiaService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AnterogradiaController(
    val anterogradiaService: AnterogradiaService
) : AnterogradiaApi {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun compileAnterogradia(body: String): ResponseEntity<String> {
        return Anterogradia.invokeCompiler(body, antgRuntime = anterogradiaService.runtimeWithAvis())
            .use<ResponseEntity<String>> { input, output, e, dump ->
                if (e != null) {
                    val cause = e.rootCause()
                    val message = "${cause::class.simpleName}: ${cause.message}"
                    logger.error(message)
                    return@use ResponseEntity(
                        message,
                        HttpStatus.UNPROCESSABLE_ENTITY
                    )
                } else
                    return@use ResponseEntity.ok(output)
            }
    }

}