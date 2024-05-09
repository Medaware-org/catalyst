package org.medaware.catalyst.persistence.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "auth_token", schema = "medaware")
class TokenEntity {

    @Id
    @GeneratedValue
    lateinit var id: UUID

    @Column(name = "user_id")
    lateinit var userId: UUID

    @Column(name = "token")
    lateinit var token: String

}