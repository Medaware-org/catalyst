package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findUserEntityByUsername(username: String): UserEntity?
    fun findUserEntityByUsernameAndPasswordHash(username: String, passwordHash: String): UserEntity?

    @Query("""select distinct e from TokenEntity t inner join UserEntity e on t.userId = e.id""")
    fun findUserEntityByToken(token: String): UserEntity?

    fun findUserEntityById(id: UUID): UserEntity?
}