package org.medaware.catalyst.error

import jakarta.servlet.http.HttpServletResponse
import org.medaware.catalyst.model.CatalystError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
@Component
class ErrorHandlerConfig {

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception, resp: HttpServletResponse): ResponseEntity<CatalystError> {
        return when (e) {
            is CatalystException -> ResponseEntity(CatalystError(e.brief, e.message, e.code.value()), e.code)
            else -> ResponseEntity(
                CatalystError(e.javaClass.simpleName, e.message ?: "An unknown error occurred", 500),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

}