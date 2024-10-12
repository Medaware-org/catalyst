package org.medaware.catalyst.service

import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.RenderTaskEntity
import org.medaware.catalyst.persistence.repository.RenderTaskRepository
import org.medaware.catalyst.service.avis.AvisInterfaceService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service

class RenderTaskService(
    val renderTaskRepository: RenderTaskRepository,
    val avisInterfaceService: AvisInterfaceService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun newestRenderTask(article: ArticleEntity): RenderTaskEntity? =
        renderTaskRepository.getFirstByArticleOrderByCreatedAtDesc(article)

    fun newestValidRenderTaskOrNull(article: ArticleEntity): RenderTaskEntity? =
        newestRenderTask(article).let {
            if (it == null || it.invalidated)
                return@let null
            return@let it
        }

    
    fun createRenderTask(article: ArticleEntity, antgResult: String, htmlResult: String): RenderTaskEntity {
        val entity = RenderTaskEntity()
        entity.article = article
        entity.antgResult = antgResult
        entity.htmlResult = htmlResult
        entity.invalidated = false
        entity.createdAt = Instant.now()
        renderTaskRepository.save(entity)
        return entity
    }

    fun render(article: ArticleEntity): RenderTaskEntity {
        logger.info("Invoking renderer for article ${article.id} \"${article.title}\" by ${article.maintainer.displayName}")

        val avisArticle = avisInterfaceService.assembleArticle(article)
        val result = avisInterfaceService.render(avisArticle)

        return createRenderTask(article, result.antg, result.html)
    }

    fun renderOrRetrieveFromCache(article: ArticleEntity): RenderTaskEntity {
        val cached = newestValidRenderTaskOrNull(article)

        if (cached != null)
            return cached

        // Cache is invalid - Re-render the article
        return render(article)
    }

    /**
     * Invalidates the newest render result for a given article, and thus the article's entire render cache
     */
    
    fun invalidateCache(article: ArticleEntity) {
        val task = newestValidRenderTaskOrNull(article) ?: return
        task.invalidated = true
        renderTaskRepository.save(task)
    }

    fun getAllTasksOf(article: ArticleEntity): List<RenderTaskEntity> = renderTaskRepository.getAllByArticle(article)

    
    fun removeAllOf(article: ArticleEntity) {
        renderTaskRepository.deleteAll(getAllTasksOf(article))
    }


}