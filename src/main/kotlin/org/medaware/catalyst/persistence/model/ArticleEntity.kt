package org.medaware.catalyst.persistence.model

import jakarta.persistence.*
import org.medaware.catalyst.dto.ArticleResponse
import java.time.Instant
import java.time.ZoneId
import java.util.*

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
    var rootElement: UUID? = null

    @Column(name = "visible")
    var visible: Boolean = false

    @ManyToOne
    @JoinColumn(name = "topic")
    lateinit var topic: TopicEntity

    fun toDto(): ArticleResponse =
        ArticleResponse(
            author = maintainer.displayName,
            title = title,
            date = createdAt.atZone(ZoneId.systemDefault()).toLocalDate(),
            id = id,
            topic = topic.toDto(),
        )
}