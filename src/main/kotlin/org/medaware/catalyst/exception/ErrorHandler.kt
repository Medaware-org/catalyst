package org.medaware.catalyst.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

data class CatalystErrorDto(
    val summary: String,
    val message: String
)

fun CatalystException.toDto() = CatalystErrorDto(this.summary, this.message)

@ControllerAdvice
class ErrorHandler {

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<CatalystErrorDto> {
        return when (exception) {
            is CatalystException -> ResponseEntity(exception.toDto(), exception.statusCode)
            else -> ResponseEntity(
                CatalystErrorDto("Error", "An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

}