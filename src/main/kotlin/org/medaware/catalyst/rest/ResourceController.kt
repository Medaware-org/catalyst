package org.medaware.catalyst.rest

import org.medaware.catalyst.openapi.controllers.ResourcesApi
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity

class ResourceController : ResourcesApi {

    override fun catalystCreateImage(designator: String, file: Resource?): ResponseEntity<Unit> {
        return super.catalystCreateImage(designator, file)
    }
}