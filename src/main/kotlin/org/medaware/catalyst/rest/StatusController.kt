package org.medaware.catalyst.rest

import org.medaware.catalyst.api.StatusApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StatusMapping : StatusApi {

    override fun catalystStatus(): ResponseEntity<String> {
        return super.catalystStatus()
    }
}