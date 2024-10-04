package org.medaware.catalyst.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.medaware.catalyst.dto.ArticleResponse
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

@Entity
@Table(name = "article")
class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    lateinit var id: UUID

    @ManyToOne
    @JoinColumn(name = "maintainer_id")
    lateinit var maintainer: MaintainerEntity

    @Column(name = "title")
    lateinit var title: String

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    @Column(name = "root_element", nullable = true)
    lateinit var rootElement: UUID

    fun toDto(): ArticleResponse =
        ArticleResponse(
            author = maintainer.displayName,
            title = title,
            date = createdAt.atZone(ZoneId.systemDefault()).toLocalDate(),
            id = id
        )

}