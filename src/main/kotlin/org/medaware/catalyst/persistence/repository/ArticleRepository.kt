package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.model.TopicEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ArticleRepository : JpaRepository<ArticleEntity, UUID> {

    fun getArticleEntityById(id: UUID): ArticleEntity?

    fun getArticleEntitiesByMaintainer(maintainer: MaintainerEntity): List<ArticleEntity>

    fun getArticleEntitiesByMaintainerId(maintainerId: UUID): List<ArticleEntity>

    fun getArticleEntitiesByTopic(topic: TopicEntity): List<ArticleEntity>

}