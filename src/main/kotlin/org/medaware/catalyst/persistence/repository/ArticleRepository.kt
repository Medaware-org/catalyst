package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.entity.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ArticleRepository : JpaRepository<ArticleEntity, UUID>