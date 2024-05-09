package org.medaware.catalyst.rest

import org.medaware.catalyst.lucene.LuceneService
import org.medaware.catalyst.model.ArticleCreationRequest
import org.medaware.catalyst.model.ArticleQueryRequest
import org.medaware.catalyst.model.BriefArticleResponse
import org.medaware.catalyst.model.UuidResponse
import org.medaware.catalyst.openapi.controllers.ArticleApi
import org.medaware.catalyst.persistence.service.ArticleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController(
    val articleService: ArticleService,
    val luceneService: LuceneService
) : ArticleApi {

    override fun catalystCreateArticle(articleCreationRequest: ArticleCreationRequest): ResponseEntity<UuidResponse> {
        return ResponseEntity.ok(UuidResponse(articleService.createArticle(articleCreationRequest)))
    }

    override fun catalystQueryArticle(query: String): ResponseEntity<List<BriefArticleResponse>> {
        return ResponseEntity.ok(articleService.search(query))
    }
}