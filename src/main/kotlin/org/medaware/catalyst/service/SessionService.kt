package org.medaware.catalyst.service

import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.model.SessionEntity
import org.medaware.catalyst.persistence.repository.SessionRepository
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Base64

@Service
class SessionService(
    val sessionRepository: SessionRepository
) {

    fun getValidSession(sessionToken: String): SessionEntity? = sessionRepository.getSessionEntityByTokenAfter(
        sessionToken,
        Instant.now().minus(24, ChronoUnit.HOURS)
    )

    fun removeAllSessionsOf(maintainer: MaintainerEntity) =
        sessionRepository.deleteAll(sessionRepository.getSessionEntitiesByMaintainer(maintainer))

    fun createSession(maintainer: MaintainerEntity): SessionEntity {
        removeAllSessionsOf(maintainer)

        val bytes = ByteArray(128)
        SecureRandom().nextBytes(bytes)
        val sessionToken = Base64.getEncoder().encodeToString(bytes)

        val entity = SessionEntity()
        entity.sessionToken = sessionToken
        entity.maintainer = maintainer
        entity.createdAt = Instant.now()
        entity.accessedAt = Instant.now()

        sessionRepository.save(entity)

        return entity
    }

}