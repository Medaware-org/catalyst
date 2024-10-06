package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SequentialElementRepository : JpaRepository<SequentialElementEntity, UUID> {

    fun getSequentialElementEntityByArticleAndId(article: ArticleEntity, id: UUID): SequentialElementEntity?

    fun getSequentialElementEntityById(id: UUID): SequentialElementEntity?

    fun getSequentialElementEntityByPrecedingElementAndArticle(
        preceding: SequentialElementEntity,
        article: ArticleEntity
    ): SequentialElementEntity?

    fun getSequentialElementEntityByArticleAndHandle(article: ArticleEntity, handle: String): SequentialElementEntity?

}