package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.RenderTaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RenderTaskRepository : JpaRepository<RenderTaskEntity, UUID>