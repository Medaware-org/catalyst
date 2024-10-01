package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.model.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.util.UUID

interface SessionRepository : JpaRepository<SessionEntity, UUID> {

    @Query("""select entity from SessionEntity entity where entity.sessionToken = :sessionToken and entity.accessedAt > :expirationDate and not entity.invalidated""")
    fun getSessionEntityByTokenAfter(sessionToken: String, expirationDate: Instant): SessionEntity?

    @Query("""select entity from SessionEntity entity where entity.maintainer = :maintainer and entity.accessedAt > :expirationDate and not entity.invalidated""")
    fun getSessionEntityOfMaintainerAfter(maintainer: MaintainerEntity, expirationDate: Instant): SessionEntity?

    @Query("""select entity from SessionEntity entity where entity.maintainer = :maintainer and not entity.invalidated""")
    fun getSessionEntitiesByMaintainer(maintainer: MaintainerEntity): List<SessionEntity>

}