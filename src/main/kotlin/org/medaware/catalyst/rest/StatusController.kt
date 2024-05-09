package org.medaware.catalyst.rest

import org.medaware.catalyst.openapi.controllers.StatusApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StatusController : StatusApi {

    override fun catalystStatus(): ResponseEntity<String> {
        return ResponseEntity.ok("Medaware Catalyst is up and running!")
    }

}