package org.medaware.catalyst.persistence.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
import java.util.*

@Entity
@Table(name = "\"user\"", schema = "medaware")
class UserEntity {

    @Id
    @GeneratedValue
    lateinit var id: UUID

    @Column(name = "user_name")
    lateinit var username: String

    @Column(name = "first_name")
    lateinit var firstName: String

    @Column(name = "last_name")
    lateinit var lastName: String

    @Column(name = "password_hash")
    lateinit var passwordHash: String

    @Column(name = "created_at")
    @CreatedDate
    var createdAt: Instant = Instant.now()

    @Column(name = "last_online")
    var lastOnline: Instant = Instant.now()

}