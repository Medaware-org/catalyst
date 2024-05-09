package org.medaware.catalyst.persistence.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "article", schema = "medaware")
class ArticleEntity {

    @Id
    @GeneratedValue
    lateinit var id: UUID

    @Column(name = "created_by")
    lateinit var createdby: UUID

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    @Column(name = "title")
    lateinit var title: String

    @Column(name = "lead")
    lateinit var lead: String

    @Column(name = "content")
    lateinit var content: String

}