package org.medaware.catalyst.service

import org.medaware.catalyst.dto.ArticleResponse
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.medaware.catalyst.security.currentSession
import org.medaware.catalyst.security.isAuthenticated
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ArticleService(
    val articleRepository: ArticleRepository,
    val sequentialElementService: SequentialElementService,
    val renderTaskService: RenderTaskService,
    val maintainerService: MaintainerService
) {

    fun getArticlesBy(maintainer: MaintainerEntity): List<ArticleEntity> =
        articleRepository.getArticleEntitiesByMaintainer(maintainer)

    fun getDtosOfArticlesBy(maintainer: MaintainerEntity) = getArticlesBy(maintainer).map { it.toDto() }

    fun getDtosOfArticlesBy(maintainerId: UUID) =
        articleRepository.getArticleEntitiesByMaintainerId(maintainerId).map { it.toDto() }

    fun getDtosOfAllArticles() = articleRepository.findAll().map { it.toDto() }

    fun getArticleById(id: UUID): ArticleEntity? = articleRepository.getArticleEntityById(id)

    fun createArticle(title: String): ArticleResponse {
        val article = ArticleEntity()
        article.title = title
        article.createdAt = Instant.now()
        article.maintainer = if (isAuthenticated()) currentSession().maintainer else maintainerService.getDefault()
        articleRepository.save(article)

        // At this point, this article does not have a root element yet. So let's create one now.
        val titleElement = sequentialElementService.insertElement(
            article, null, "title", "HEADING", meta = arrayOf(
                "TEXT" to title
            )
        )

        sequentialElementService.insertElement(article, titleElement.id, "blank-element")

        article.rootElement = titleElement.id
        articleRepository.save(article)

        return article.toDto()
    }

    fun detachRoot(article: ArticleEntity) {
        article.rootElement = null
        articleRepository.save(article)
    }

    fun deleteArticle(article: ArticleEntity) {
        var tmpArticle = article

        val elements = sequentialElementService.findAllElementsOfArticle(tmpArticle)
        renderTaskService.removeAllOf(tmpArticle)
        detachRoot(tmpArticle)

        for (element in elements.reversed())
            sequentialElementService.deleteElement(element, allowRootDeletion = true)

        articleRepository.delete(article)
    }

}