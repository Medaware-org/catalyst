package org.medaware.catalyst.api

import org.medaware.catalyst.dto.LoginRequest
import org.medaware.catalyst.dto.RegistrationRequest
import org.medaware.catalyst.dto.TokenResponse
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class AuthenticationApiTest {

    private val api: AuthenticationApiController = AuthenticationApiController()

    /**
     * To test AuthenticationApiController.catalystLogin
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun catalystLoginTest() {
        val loginRequest: LoginRequest = TODO()
        val response: ResponseEntity<TokenResponse> = api.catalystLogin(loginRequest)

        // TODO: test validations
    }

    /**
     * To test AuthenticationApiController.catalystLogout
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun catalystLogoutTest() {
        val response: ResponseEntity<Unit> = api.catalystLogout()

        // TODO: test validations
    }

    /**
     * To test AuthenticationApiController.catalystRegister
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun catalystRegisterTest() {
        val registrationRequest: RegistrationRequest = TODO()
        val response: ResponseEntity<Unit> = api.catalystRegister(registrationRequest)

        // TODO: test validations
    }
}
