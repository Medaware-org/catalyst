package org.medaware.catalyst.service

import jakarta.annotation.PostConstruct
import org.medaware.catalyst.config.catalyst.CatalystAccessConfiguration
import org.medaware.catalyst.dto.AccountUpdateRequest
import org.medaware.catalyst.dto.TangentialLoginRequest
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.model.SessionEntity
import org.medaware.catalyst.persistence.repository.MaintainerRepository
import org.medaware.catalyst.security.currentSession
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class MaintainerService(
    val maintainerRepository: MaintainerRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder,
    val catalystAccessConfiguration: CatalystAccessConfiguration,
    val sessionService: SessionService
) {

    fun byId(id: UUID): MaintainerEntity? =
        maintainerRepository.getMaintainerEntityById(id)

    @PostConstruct
    @Transactional
    fun createDefaultMaintainer() {
        if (maintainerExists(catalystAccessConfiguration.username))
            return

        createMaintainer(
            "Default",
            "Maintainer",
            catalystAccessConfiguration.username,
            "System Administrator",
            catalystAccessConfiguration.password
        )
    }

    fun getDefault(): MaintainerEntity =
        maintainerRepository.getMaintainerEntityByUsername(catalystAccessConfiguration.username)!!

    fun maintainerExists(username: String): Boolean =
        maintainerRepository.getMaintainerEntityByUsername(username) != null

    fun createMaintainer(firstName: String, lastName: String, username: String, displayName: String, password: String) {
        if (maintainerExists(username))
            throw CatalystException(
                "Username Taken",
                "The username \"${username}\" is already taken",
                HttpStatus.CONFLICT
            )

        val entity = MaintainerEntity()
        entity.firstName = firstName
        entity.lastName = lastName
        entity.username = username
        entity.displayName = displayName
        entity.passwordHash = bCryptPasswordEncoder.encode(password)
        entity.createdAt = Instant.now()
        maintainerRepository.save(entity)
    }

    fun login(username: String, password: String): SessionEntity {
        val maintainer = maintainerRepository.getMaintainerEntityByUsername(username)
        val exception = CatalystException("Login Failed", "Login failed: Invalid credentials", HttpStatus.UNAUTHORIZED)

        if (maintainer == null)
            throw exception

        if (!bCryptPasswordEncoder.matches(password, maintainer.passwordHash))
            throw exception

        return sessionService.createSession(maintainer)
    }

    fun login(request: TangentialLoginRequest) = login(request.username, request.password)

    fun currentMaintainer() = currentSession().maintainer

    fun updateAccountDetails(maintainer: MaintainerEntity, update: AccountUpdateRequest) {
        if (update.firstName != null)
            maintainer.firstName = update.firstName

        if (update.lastName != null)
            maintainer.lastName = update.lastName

        if (update.displayName != null)
            maintainer.displayName = update.displayName

        maintainerRepository.save(maintainer)
    }

}