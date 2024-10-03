package org.medaware.catalyst.rest

import org.medaware.catalyst.api.SecurityApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController : SecurityApi {

    override fun securedRoute(): ResponseEntity<String> {
        return ResponseEntity.ok(StatusController.STATUS_MESSAGE)
    }

}