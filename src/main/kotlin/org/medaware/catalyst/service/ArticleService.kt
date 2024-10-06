package org.medaware.catalyst.service

import jakarta.transaction.Transactional
import org.medaware.catalyst.dto.ArticleResponse
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.medaware.catalyst.security.currentSession
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
@Transactional
class ArticleService(
    val articleRepository: ArticleRepository,
    val sequentialElementService: SequentialElementService
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
        article.maintainer = currentSession().maintainer
        articleRepository.save(article)

        // At this point, this article does not have a root element yet. So let's create one now.
        val title = sequentialElementService.insertBlankElement(article, null, "root")

        article.rootElement = title.id
        articleRepository.save(article)

        return article.toDto()
    }

}