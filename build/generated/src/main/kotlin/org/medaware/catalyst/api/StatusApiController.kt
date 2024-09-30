package org.medaware.catalyst.api

import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.enums.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.security.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

import kotlin.collections.List
import kotlin.collections.Map

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class StatusApiController() {

    @Operation(
        summary = "A status endpoint",
        operationId = "catalystStatus",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Medaware Catalyst is up and running", content = [Content(schema = Schema(implementation = kotlin.String::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/"],
        produces = ["text/plain"]
    )
    fun catalystStatus(): ResponseEntity<kotlin.String> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
