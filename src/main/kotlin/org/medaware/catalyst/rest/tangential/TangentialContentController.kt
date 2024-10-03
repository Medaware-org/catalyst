package org.medaware.catalyst.rest.tangential

import org.medaware.catalyst.api.TangentialContentApi
import org.medaware.catalyst.dto.ArticleResponse
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.security.currentSession
import org.medaware.catalyst.service.ArticleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class TangentialContentController (
    val articleService: ArticleService
) : TangentialContentApi {

    override fun listArticles(selector: String): ResponseEntity<List<ArticleResponse>> {
        val uuid = try {
            UUID.fromString(selector)
        } catch (e: IllegalArgumentException) {
            null
        }

        if (uuid != null)
            return ResponseEntity.ok(articleService.getDtosOfArticlesBy(uuid))

        return ResponseEntity.ok(
            when (selector.lowercase()) {
                "current" -> articleService.getDtosOfArticlesBy(currentSession().maintainer)
                "all" -> articleService.getDtosOfAllArticles()
                else -> throw CatalystException(
                    "Unknown Selector",
                    "The article listing selector '${selector}' is undefined.",
                    HttpStatus.UNPROCESSABLE_ENTITY
                )
            }
        )
    }

}