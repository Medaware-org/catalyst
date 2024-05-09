package org.medaware.catalyst.persistence.service

import org.medaware.catalyst.error.CatalystException
import org.medaware.catalyst.model.LoginRequest
import org.medaware.catalyst.model.RegistrationRequest
import org.medaware.catalyst.persistence.entity.UserEntity
import org.medaware.catalyst.persistence.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    val userRepository: UserRepository,
    val bcrypt: BCryptPasswordEncoder,
    val tokenService: TokenService
) {

    fun userExists(userId: UUID): Boolean {
        return userRepository.findUserEntityById(userId) != null
    }

    fun createUser(req: RegistrationRequest) {
        userRepository.findUserEntityByUsername(req.userName)?.let {
            throw CatalystException(
                "User already exists",
                "The username ${req.userName} is already taken",
                HttpStatus.CONFLICT
            )
        }

        val entity = UserEntity()
        entity.username = req.userName
        entity.firstName = req.firstName
        entity.lastName = req.lastName
        entity.passwordHash = bcrypt.encode(req.password)

        userRepository.save(entity)
    }

    fun loginUser(req: LoginRequest): String {
        (userRepository.findUserEntityByUsernameAndPasswordHash(req.userName, bcrypt.encode(req.password)) ?: run {
            throw CatalystException(
                "Login failed",
                "The credentials do not match any existing user",
                HttpStatus.UNAUTHORIZED
            )
        }).let {
            return tokenService.provideToken(it.id)
        }
    }

}