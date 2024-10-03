package org.medaware.catalyst.service

import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ArticleService(
    val articleRepository: ArticleRepository
) {

    fun getArticlesBy(maintainer: MaintainerEntity): List<ArticleEntity> = articleRepository.getArticleEntitiesByMaintainer(maintainer)

    fun getDtosOfArticlesBy(maintainer: MaintainerEntity) = getArticlesBy(maintainer).map { it.toDto() }

    fun getDtosOfArticlesBy(maintainerId: UUID) = articleRepository.getArticleEntitiesByMaintainerId(maintainerId).map { it.toDto() }

    fun getDtosOfAllArticles() = articleRepository.findAll().map { it.toDto() }

}