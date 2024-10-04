package org.medaware.catalyst.service

import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.medaware.catalyst.config.CatalystConfiguration
import org.medaware.catalyst.dto.TangentialLoginRequest
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.model.SessionEntity
import org.medaware.catalyst.persistence.repository.MaintainerRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
@Transactional
class MaintainerService(
    val maintainerRepository: MaintainerRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder,
    val catalystConfiguration: CatalystConfiguration,
    val sessionService: SessionService
) {

    @PostConstruct
    fun createDefaultMaintainer() {
        if (maintainerExists(catalystConfiguration.username))
            return

        createMaintainer(
            "Default",
            "Maintainer",
            catalystConfiguration.username,
            "System Administrator",
            catalystConfiguration.password
        )
    }

    fun maintainerExists(username: String): Boolean =
        maintainerRepository.getMaintainerEntityByUsername(username) != null

    fun createMaintainer(firstName: String, lastName: String, username: String, displayName: String, password: String) {
        if (maintainerExists(username))
            throw CatalystException("Username Taken", "The username \"${username}\" is already taken", HttpStatus.CONFLICT)

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

}