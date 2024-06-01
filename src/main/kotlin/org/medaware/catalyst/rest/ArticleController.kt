package org.medaware.catalyst.rest

import org.medaware.catalyst.model.UuidResponse
import org.medaware.catalyst.openapi.controllers.ArticleApi
import org.medaware.catalyst.persistence.service.ArticleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController(
    val articleService: ArticleService
) : ArticleApi {

    override fun catalystCreateArticle(title: String, lead: String, html: String): ResponseEntity<UuidResponse> {
        return ResponseEntity.ok(UuidResponse(articleService.createArticle(title, lead, html)))
    }

}