package org.medaware.catalyst.service

import org.medaware.catalyst.dto.ArticleResponse
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.medaware.catalyst.security.currentSession
import org.medaware.catalyst.security.isAuthenticated
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ArticleService(
    val articleRepository: ArticleRepository,
    val sequentialElementService: SequentialElementService,
    val renderTaskService: RenderTaskService,
    val metadataService: MetadataService,
    val maintainerService: MaintainerService,
    val topicService: TopicService,
    val luceneService: LuceneService
) {

    fun getArticlesBy(maintainer: MaintainerEntity): List<ArticleEntity> =
        articleRepository.getArticleEntitiesByMaintainer(maintainer)

    fun getDtosOfArticlesBy(maintainer: MaintainerEntity) = getArticlesBy(maintainer).map { it.toDto() }

    fun getAllArticles() = articleRepository.findAll()

    fun getDtosOfArticlesBy(maintainerId: UUID) =
        articleRepository.getArticleEntitiesByMaintainerId(maintainerId).map { it.toDto() }

    fun getDtosOfAllArticles() = articleRepository.findAll().map { it.toDto() }

    fun getArticleById(id: UUID): ArticleEntity? = articleRepository.getArticleEntityById(id)

    /**
     * This functions dumps the entire article with all of its elements into an indexable string.
     * TODO Improve this
     */
    fun dumpToString(article: ArticleEntity): String {
        val elements = sequentialElementService.findAllElementsOfArticle(article)
        val articleStr = StringBuilder()
        val ignoreKeys = arrayOf("ELEMENT_TYPE")
        elements.forEach {
            metadataService.getMetadataOf(it).forEach {
                if (ignoreKeys.contains(it.key))
                    return@forEach

                articleStr.append(it.value).append(" ")
            }
        }
        return articleStr.toString()
    }

    fun createArticle(title: String): ArticleResponse {
        val article = ArticleEntity()
        article.title = title
        article.createdAt = Instant.now()
        article.maintainer = if (isAuthenticated()) currentSession().maintainer else maintainerService.getDefault()
        article.topic = topicService.getFallbackTopic()
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

        luceneService.indexArticle(article)

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

    fun updateArticle(article: ArticleEntity, title: String?, topic: UUID?) {
        if (title != null)
            article.title = title

        if (topic != null) {
            val topic = topicService.retrieveTopic(topic)
            article.topic = topic
        }

        articleRepository.save(article)
    }

}