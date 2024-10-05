package org.medaware.catalyst.rest.tangential

import org.medaware.catalyst.api.TangentialContentApi
import org.medaware.catalyst.dto.ArticleCreationRequest
import org.medaware.catalyst.dto.ArticleResponse
import org.medaware.catalyst.dto.MetadataCreateRequest
import org.medaware.catalyst.dto.MetadataEntry
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.security.currentSession
import org.medaware.catalyst.service.ArticleService
import org.medaware.catalyst.service.MetadataService
import org.medaware.catalyst.service.RenderTaskService
import org.medaware.catalyst.service.SequentialElementService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class TangentialContentController(
    val articleService: ArticleService,
    val metadataService: MetadataService,
    val elementService: SequentialElementService,
    val renderTaskService: RenderTaskService
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

    override fun createArticle(articleCreationRequest: ArticleCreationRequest): ResponseEntity<ArticleResponse> {
        return ResponseEntity.ok(articleService.createArticle(articleCreationRequest.title))
    }

    override fun getMetadata(elementId: UUID): ResponseEntity<List<MetadataEntry>> {
        val element = elementService.getById(elementId) ?: throw CatalystException(
            "Element Not Found",
            "The element '${elementId}' does not exist.",
            HttpStatus.NOT_FOUND
        )

        return ResponseEntity.ok(metadataService.getMetadataAsDtosOf(element))
    }

    override fun putMetadata(
        elementId: UUID,
        metadataCreateRequest: MetadataCreateRequest
    ): ResponseEntity<MetadataEntry> {
        val element = elementService.getById(elementId) ?: throw CatalystException(
            "Element Not Found",
            "The element '${elementId}' does not exist.",
            HttpStatus.NOT_FOUND
        )
        val meta = metadataService.putMetaEntry(element, metadataCreateRequest.key, metadataCreateRequest.value)
        return ResponseEntity.ok(meta.toDto())
    }

    override fun renderArticle(id: UUID): ResponseEntity<String> {
        val article = articleService.getArticleById(id) ?: throw CatalystException(
            "Article Not Found",
            "The requested article '${id}' does not exist.",
            HttpStatus.NOT_FOUND
        )

        return ResponseEntity.ok(renderTaskService.renderOrRetrieveFromCache(article).htmlResult)
    }
}