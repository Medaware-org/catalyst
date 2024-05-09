package org.medaware.catalyst.rest.openapi

import jakarta.servlet.http.HttpServletResponse
import org.medaware.catalyst.error.CatalystException
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files

@RestController
class OpenApiSpecController {

    @GetMapping(path = ["/spec.yaml"], produces = ["application/yaml"])
    @ResponseBody
    fun retrieveOpenApiSpecification(response: HttpServletResponse): ResponseEntity<String> {
        val resource = ClassPathResource("static/spec.yaml")

        if (!resource.exists())
            throw CatalystException(
                "File not found",
                "Could not find the OpenAPI specification at the standard location",
                HttpStatus.NOT_FOUND
            )

        val spec = Files.readString(resource.file.toPath())

        return ResponseEntity.ok(spec)
    }

}