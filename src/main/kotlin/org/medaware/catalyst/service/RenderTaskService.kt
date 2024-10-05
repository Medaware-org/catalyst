package org.medaware.catalyst.service

import jakarta.transaction.Transactional
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.RenderTaskEntity
import org.medaware.catalyst.persistence.repository.RenderTaskRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
@Transactional
class RenderTaskService(
    val renderTaskRepository: RenderTaskRepository
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

    /**
     * Invalidates the newest render result for a given article, and thus the article's entire render cache
     */
    fun invalidateCache(article: ArticleEntity) {
        val task = newestValidRenderTaskOrNull(article) ?: return
        task.invalidated = true
        renderTaskRepository.save(task)
    }

    fun getAllTasksOf(article: ArticleEntity): List<RenderTaskEntity> = renderTaskRepository.getAllByArticle(article)

}