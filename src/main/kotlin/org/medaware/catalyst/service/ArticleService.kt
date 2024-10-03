package org.medaware.catalyst.service

import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.springframework.stereotype.Service

@Service
class ArticleService(
    val articleRepository: ArticleRepository
) {

    fun getArticlesWrittenBy(maintainer: MaintainerEntity): List<ArticleEntity> = articleRepository.getArticleEntitiesByMaintainer(maintainer)

    fun getDtosOfArticlesWrittenBy(maintainer: MaintainerEntity) = getArticlesWrittenBy(maintainer).map { it.toDto() }

}