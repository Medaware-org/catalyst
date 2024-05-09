package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.entity.TokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TokenRepository : JpaRepository<TokenEntity, UUID> {

    fun findTokenEntityByUserId(userId: UUID): List<TokenEntity>

    fun findTokenEntityByToken(token: String): TokenEntity?

}