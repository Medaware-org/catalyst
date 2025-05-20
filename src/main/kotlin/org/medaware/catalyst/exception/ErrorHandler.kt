package org.medaware.catalyst.exception

import org.medaware.catalyst.dto.CatalystError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.net.ConnectException


fun CatalystException.toDto() = CatalystError(this.summary, this.message)

@ControllerAdvice
class ErrorHandler {

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<CatalystError> {
        return when (exception) {
            is CatalystException -> ResponseEntity(exception.toDto(), exception.statusCode)
            is HttpRequestMethodNotSupportedException -> ResponseEntity(
                CatalystError(
                    "Method Not Supported",
                    exception.message ?: "The method '${exception.method}' is not supported"
                ), exception.statusCode
            )

            is NoResourceFoundException -> ResponseEntity(
                CatalystError(
                    "No such route",
                    "The route '/${exception.resourcePath}' does not exist"
                ), HttpStatus.NOT_FOUND
            )

            else -> {
                exception.printStackTrace()
                return ResponseEntity(
                    CatalystError("Error", "An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR
                )
            }
        }
    }

}