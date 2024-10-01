package org.medaware.catalyst.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.medaware.catalyst.dto.TangentialSessionResponse
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "session")
class SessionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @Column(name = "session_token")
    lateinit var sessionToken: String

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    @Column(name = "accessed_at")
    lateinit var accessedAt: Instant

    @ManyToOne
    @JoinColumn(name = "maintainer_id")
    lateinit var maintainer: MaintainerEntity

    fun toDto(): TangentialSessionResponse = TangentialSessionResponse(sessionToken)

}