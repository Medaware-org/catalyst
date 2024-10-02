package org.medaware.catalyst.exception

import org.medaware.catalyst.dto.CatalystError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


fun CatalystException.toDto() = CatalystError(this.summary, this.message)

@ControllerAdvice
class ErrorHandler {

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<CatalystError> {
        return when (exception) {
            is CatalystException -> ResponseEntity(exception.toDto(), exception.statusCode)
            else -> ResponseEntity(
                CatalystError("Error", "An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

}