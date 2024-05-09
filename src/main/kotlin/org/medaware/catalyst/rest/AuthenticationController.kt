package org.medaware.catalyst.rest

import org.medaware.catalyst.model.LoginRequest
import org.medaware.catalyst.model.RegistrationRequest
import org.medaware.catalyst.model.TokenResponse
import org.medaware.catalyst.openapi.controllers.AuthenticationApi
import org.medaware.catalyst.persistence.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(
    val userService: UserService
) : AuthenticationApi {

    override fun catalystLogin(loginRequest: LoginRequest): ResponseEntity<TokenResponse> {
        return super.catalystLogin(loginRequest)
    }

    override fun catalystRegister(registrationRequest: RegistrationRequest): ResponseEntity<Unit> {
        userService.createUser(registrationRequest)
        return ResponseEntity.ok().build()
    }
}