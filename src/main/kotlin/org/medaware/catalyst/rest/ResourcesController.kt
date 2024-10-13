package org.medaware.catalyst.rest

import org.medaware.catalyst.api.ResourcesApi
import org.medaware.catalyst.service.MinIoService
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.security.util.InMemoryResource
import org.springframework.web.bind.annotation.RestController

@RestController
class ResourcesController(
    val minIoService: MinIoService
) : ResourcesApi {

    override fun getResource(id: String): ResponseEntity<Resource> {
        return ResponseEntity.ok(InMemoryResource(minIoService.retrieve(id)))
    }

}