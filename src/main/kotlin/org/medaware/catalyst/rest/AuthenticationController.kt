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
        val token = userService.loginUser(loginRequest)
        return ResponseEntity.ok(TokenResponse(token))
    }

    override fun catalystRegister(registrationRequest: RegistrationRequest): ResponseEntity<Unit> {
        userService.createUser(registrationRequest)
        return ResponseEntity.ok().build()
    }

    override fun catalystLogout(): ResponseEntity<Unit> {
        userService.logoutUser()
        return ResponseEntity.ok().build()
    }

}