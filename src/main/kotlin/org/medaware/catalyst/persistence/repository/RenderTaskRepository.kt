package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.RenderTaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RenderTaskRepository : JpaRepository<RenderTaskEntity, UUID> {

    /**
     * Find the newest render task of a given article
     */
    fun getFirstByArticleOrderByCreatedAtDesc(article: ArticleEntity): RenderTaskEntity?

    fun getAllByArticle(article: ArticleEntity): List<RenderTaskEntity>

}