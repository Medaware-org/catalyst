package org.medaware.catalyst.service

import org.medaware.catalyst.persistence.model.SessionEntity
import org.medaware.catalyst.persistence.repository.SessionRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class SessionService(
    val sessionRepository: SessionRepository
) {

    fun getValidSession(sessionToken: String): SessionEntity? = sessionRepository.getSessionEntityByTokenAfter(
        sessionToken,
        Instant.now().minus(24, ChronoUnit.HOURS)
    )

}