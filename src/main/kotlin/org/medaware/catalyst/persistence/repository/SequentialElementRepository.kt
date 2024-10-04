package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SequentialElementRepository : JpaRepository<SequentialElementEntity, UUID>