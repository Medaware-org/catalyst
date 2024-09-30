package org.medaware.catalyst.api

import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class StatusApiTest {

    private val api: StatusApiController = StatusApiController()

    /**
     * To test StatusApiController.catalystStatus
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun catalystStatusTest() {
        val response: ResponseEntity<kotlin.String> = api.catalystStatus()

        // TODO: test validations
    }
}
