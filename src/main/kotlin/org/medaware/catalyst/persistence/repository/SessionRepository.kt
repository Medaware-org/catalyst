package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SessionRepository : JpaRepository<SessionEntity, UUID>