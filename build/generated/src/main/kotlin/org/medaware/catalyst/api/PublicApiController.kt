package org.medaware.catalyst.api

import org.medaware.catalyst.dto.BriefArticleResponse
import org.medaware.catalyst.dto.CatalystCreateArticleRequest
import org.medaware.catalyst.dto.CatalystError
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
class PublicApiController() {

    @Operation(
        summary = "Retrieve all articles",
        operationId = "catalystGetAllArticles",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(implementation = BriefArticleResponse::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/public/articles"],
        produces = ["application/json"]
    )
    fun catalystGetAllArticles(): ResponseEntity<List<BriefArticleResponse>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Retrieve a full article by ID",
        operationId = "catalystGetArticle",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(implementation = CatalystCreateArticleRequest::class))]),
            ApiResponse(responseCode = "404", description = "Article not found", content = [Content(schema = Schema(implementation = CatalystError::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/public/article"],
        produces = ["application/json"]
    )
    fun catalystGetArticle(@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "id", required = true) id: java.util.UUID): ResponseEntity<CatalystCreateArticleRequest> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Search existing articles by querying lucene",
        operationId = "catalystQueryArticle",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content(schema = Schema(implementation = BriefArticleResponse::class))]) ]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/public/search"],
        produces = ["application/json"]
    )
    fun catalystQueryArticle(@NotNull @Parameter(description = "", required = true) @Valid @RequestParam(value = "query", required = true) query: kotlin.String): ResponseEntity<List<BriefArticleResponse>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
