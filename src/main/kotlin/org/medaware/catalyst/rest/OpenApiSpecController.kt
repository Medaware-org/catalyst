package org.medaware.catalyst.rest

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.ResourceLoader
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OpenApiSpecController(val resourceLoader: ResourceLoader) {

    @GetMapping("/catalyst-api-docs", produces = ["text/plain"])
    fun serveYamlSpec(): ResponseEntity<String> {
        return ResponseEntity.ok(ClassPathResource("spec.yaml").getContentAsString(Charsets.UTF_8))
    }

}