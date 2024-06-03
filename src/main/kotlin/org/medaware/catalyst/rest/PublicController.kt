package org.medaware.catalyst.rest

import org.medaware.catalyst.model.BriefArticleResponse
import org.medaware.catalyst.model.CatalystCreateArticleRequest
import org.medaware.catalyst.openapi.controllers.PublicApi
import org.medaware.catalyst.persistence.service.ArticleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class PublicController(
    val articleService: ArticleService
) : PublicApi {

    override fun catalystQueryArticle(query: String): ResponseEntity<List<BriefArticleResponse>> {
        return ResponseEntity.ok(articleService.search(query))
    }

    override fun catalystGetArticle(id: UUID): ResponseEntity<CatalystCreateArticleRequest> {
        val art = articleService.byId(id)
        return ResponseEntity.ok(CatalystCreateArticleRequest(art[0], art[1], art[2]))
    }

    override fun catalystGetAllArticles(): ResponseEntity<List<BriefArticleResponse>> {
        return ResponseEntity.ok(articleService.allArticles().map {
            BriefArticleResponse(it.title, it.lead, it.id)
        })
    }
}