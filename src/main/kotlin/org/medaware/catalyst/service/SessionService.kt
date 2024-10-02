package org.medaware.catalyst.service

import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.model.SessionEntity
import org.medaware.catalyst.persistence.repository.SessionRepository
import org.medaware.catalyst.security.currentSession
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Base64

@Service
class SessionService(
    val sessionRepository: SessionRepository
) {

    private fun tokenValidityBorder(): Instant = Instant.now().minus(24, ChronoUnit.HOURS)

    fun getValidSession(sessionToken: String): SessionEntity? = sessionRepository.getSessionEntityByTokenAfter(
        sessionToken,
        tokenValidityBorder()
    )

    fun getValidSessionOf(maintainer: MaintainerEntity) = sessionRepository.getSessionEntityOfMaintainerAfter(
        maintainer,
        tokenValidityBorder()
    )

    fun invalidateAllSessionsOf(maintainer: MaintainerEntity) {
        sessionRepository.getSessionEntitiesByMaintainer(maintainer).forEach {
            it.invalidated = true
            sessionRepository.save(it)
        }
    }

    fun invalidateCurrentSession() {
        val session = currentSession()
        session.invalidated = true
        sessionRepository.save(session)
    }

    fun createSession(maintainer: MaintainerEntity): SessionEntity {
        val previousSession = getValidSessionOf(maintainer)

        if (previousSession != null)
            return previousSession

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