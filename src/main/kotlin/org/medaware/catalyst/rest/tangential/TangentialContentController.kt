package org.medaware.catalyst.rest.tangential

import org.medaware.avis.AvisMeta
import org.medaware.catalyst.api.TangentialContentApi
import org.medaware.catalyst.dto.*
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.medaware.catalyst.security.currentSession
import org.medaware.catalyst.service.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class TangentialContentController(
    val articleService: ArticleService,
    val metadataService: MetadataService,
    val elementService: SequentialElementService,
    val renderTaskService: RenderTaskService,
    private val luceneService: LuceneService
) : TangentialContentApi {

    private fun retrieveElementById(id: UUID): SequentialElementEntity {
        return elementService.getById(id) ?: throw CatalystException(
            "Element Not Found",
            "The element '$id' does not exist.",
            HttpStatus.NOT_FOUND
        )
    }

    private fun retrieveArticleById(id: UUID): ArticleEntity {
        return articleService.getArticleById(id) ?: throw CatalystException(
            "Article Not Found",
            "The requested article '${id}' does not exist.",
            HttpStatus.NOT_FOUND
        )
    }

    override fun listArticles(selector: String, query: String?): ResponseEntity<List<ArticleResponse>> {
        val uuid = try {
            UUID.fromString(selector)
        } catch (e: IllegalArgumentException) {
            null
        }

        if (uuid != null)
            return ResponseEntity.ok(articleService.getDtosOfArticlesBy(uuid))

        if (query == null || query.trim().length < 3)
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

        val queriedArticles = luceneService.queryArticles(query)

        if (selector.lowercase() == "current")
            return ResponseEntity.ok(queriedArticles.filter { it.authorId == currentSession().maintainer.id })

        return ResponseEntity.ok(queriedArticles)
    }

    override fun createArticle(articleCreationRequest: ArticleCreationRequest): ResponseEntity<ArticleResponse> {
        return ResponseEntity.ok(articleService.createArticle(articleCreationRequest.title))
    }

    override fun getMetadata(elementId: UUID): ResponseEntity<List<MetadataEntry>> {
        val element = retrieveElementById(elementId)
        return ResponseEntity.ok(metadataService.getMetadataAsDtosOf(element))
    }

    override fun putMetadata(
        elementId: UUID,
        metadataCreateRequest: MetadataCreateRequest
    ): ResponseEntity<MetadataEntry> {
        val element = retrieveElementById(elementId)
        val meta = metadataService.putMetaEntry(element, metadataCreateRequest.key, metadataCreateRequest.value)
        return ResponseEntity.ok(meta.toDto())
    }

    override fun renderArticle(id: UUID): ResponseEntity<String> {
        val article = retrieveArticleById(id)
        return ResponseEntity.ok(renderTaskService.render(article).htmlResult)
    }

    override fun fetchArticle(id: UUID): ResponseEntity<String> {
        val article = retrieveArticleById(id)
        return ResponseEntity.ok(renderTaskService.renderOrRetrieveFromCache(article).htmlResult)
    }

    override fun getArticleElements(id: UUID): ResponseEntity<List<ElementResponse>> {
        val article = retrieveArticleById(id)
        return ResponseEntity.ok(
            elementService.findAllElementsOfArticle(article).map { it.toDto() })
    }

    override fun insertElement(elementInsertRequest: ElementInsertRequest): ResponseEntity<ElementResponse> {
        val article = retrieveArticleById(elementInsertRequest.article)

        if (!AvisMeta.ELEMENT_TYPE.valueConstraints!!.contains(elementInsertRequest.type ?: ""))
            throw CatalystException(
                "Invalid Element Type",
                "The element type \"${elementInsertRequest.type}\" does not exist.",
                HttpStatus.UNPROCESSABLE_ENTITY
            )

        var element: SequentialElementEntity = elementService.insertElement(
            article,
            elementInsertRequest.after,
            elementInsertRequest.handle,
            elementInsertRequest.type ?: "BLANK_PLACEHOLDER"
        )

        elementService.switchElementToType(element, elementInsertRequest.type!!)

        return ResponseEntity.ok(
            element.toDto()
        )
    }

    override fun alterElement(alterElementRequest: AlterElementRequest): ResponseEntity<Unit> {
        val element = retrieveElementById(alterElementRequest.id)
        elementService.switchElementToType(element, alterElementRequest.type)
        return ResponseEntity.ok().build()
    }

    override fun deleteElement(deleteElementRequest: DeleteElementRequest): ResponseEntity<Unit> {
        val element = retrieveElementById(deleteElementRequest.id)
        elementService.deleteElement(element)
        return ResponseEntity.ok().build()
    }

    override fun deleteArticle(deleteArticleRequest: DeleteArticleRequest): ResponseEntity<Unit> {
        val article = retrieveArticleById(deleteArticleRequest.id)
        articleService.deleteArticle(article)
        return ResponseEntity.ok().build()
    }

    override fun updateArticle(updateArticleRequest: UpdateArticleRequest): ResponseEntity<Unit> {
        val article = retrieveArticleById(updateArticleRequest.id)
        articleService.updateArticle(article, updateArticleRequest.title, updateArticleRequest.topic)
        return ResponseEntity.ok().build()
    }

}