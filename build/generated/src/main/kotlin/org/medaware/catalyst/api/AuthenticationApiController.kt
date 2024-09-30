package org.medaware.catalyst.api

import org.medaware.catalyst.dto.LoginRequest
import org.medaware.catalyst.dto.RegistrationRequest
import org.medaware.catalyst.dto.TokenResponse
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
class AuthenticationApiController() {

    @Operation(
        summary = "Provide authentication facilities for existing users",
        operationId = "catalystLogin",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Login successful", content = [Content(schema = Schema(implementation = TokenResponse::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/login"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun catalystLogin(@Parameter(description = "", required = true) @Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Log out a logged-in user",
        operationId = "catalystLogout",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "OK") ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/logout"]
    )
    fun catalystLogout(): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Register a new Medaware user",
        operationId = "catalystRegister",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "User successfully registered") ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/register"],
        consumes = ["application/json"]
    )
    fun catalystRegister(@Parameter(description = "", required = true) @Valid @RequestBody registrationRequest: RegistrationRequest): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
