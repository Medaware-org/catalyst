package org.medaware.catalyst.api

import org.medaware.catalyst.dto.UuidResponse
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.enums.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.security.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

import kotlin.collections.List
import kotlin.collections.Map

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class ArticleApiController() {

    @Operation(
        summary = "Create a new article",
        operationId = "catalystCreateArticle",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(implementation = UuidResponse::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/article"],
        produces = ["application/json"],
        consumes = ["multipart/form-data"]
    )
    fun catalystCreateArticle(@Parameter(description = "", required = true) @RequestParam(value = "title", required = true) title: kotlin.String ,@Parameter(description = "", required = true) @RequestParam(value = "lead", required = true) lead: kotlin.String ,@Parameter(description = "", required = true) @RequestParam(value = "html", required = true) html: kotlin.String ): ResponseEntity<UuidResponse> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
