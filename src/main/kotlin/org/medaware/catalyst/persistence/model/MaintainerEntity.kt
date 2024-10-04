package org.medaware.catalyst.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.medaware.catalyst.dto.BasicMaintainerResponse
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "maintainer")
class MaintainerEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @Column(name = "first_name")
    lateinit var firstName: String

    @Column(name = "last_name")
    lateinit var lastName: String

    @Column(name = "username")
    lateinit var username: String

    @Column(name = "display_name")
    lateinit var displayName: String

    @Column(name = "password_hash")
    lateinit var passwordHash: String

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    fun toBasicDto(): BasicMaintainerResponse =
        BasicMaintainerResponse(firstName, lastName, displayName)

}