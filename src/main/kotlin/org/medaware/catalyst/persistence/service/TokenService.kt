package org.medaware.catalyst.persistence.service

import org.medaware.catalyst.error.CatalystException
import org.medaware.catalyst.persistence.entity.TokenEntity
import org.medaware.catalyst.persistence.entity.UserEntity
import org.medaware.catalyst.persistence.repository.TokenRepository
import org.medaware.catalyst.persistence.repository.UserRepository
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*
import kotlin.random.Random

@Service
class TokenService(
    val tokenRepository: TokenRepository,

    @Lazy
    val userService: UserService,

    @Lazy
    val serRepository: TokenRepository,
    private val userRepository: UserRepository
) {
    private fun createToken(token: String, userId: UUID) {
        val entity = TokenEntity()
        entity.token = token
        entity.userId = userId

        tokenRepository.save(entity)
    }

    fun provideToken(userId: UUID): String {
        if (!userService.userExists(userId))
            throw CatalystException("Error", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR)

        val tokens = tokenRepository.findTokenEntityByUserId(userId)

        if (tokens.isNotEmpty())
            return tokens.first().token

        val bytes = Random.Default.nextBytes(128)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun userOfToken(token: String): UserEntity? {
        return userRepository.findUserEntityByToken(token)
    }

    fun valid(token: String): Boolean {
        return tokenRepository.findTokenEntityByToken(token) != null
    }

}