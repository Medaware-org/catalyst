package org.medaware.catalyst.rest

import org.medaware.catalyst.api.StatusApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StatusController : StatusApi {

    companion object {
        const val STATUS_MESSAGE = "Medaware Catalyst is up and running !"
    }

    override fun catalystStatus(): ResponseEntity<String> {
        return ResponseEntity.ok(STATUS_MESSAGE)
    }

}