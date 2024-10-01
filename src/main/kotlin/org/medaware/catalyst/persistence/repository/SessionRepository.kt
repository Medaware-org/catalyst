package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.util.UUID

interface SessionRepository : JpaRepository<SessionEntity, UUID> {

    @Query("""select entity from SessionEntity entity where entity.sessionToken = :sessionToken and entity.accessedAt > :expirationDate""")
    fun getSessionEntityByTokenAfter(sessionToken: String, expirationDate: Instant): SessionEntity?

}