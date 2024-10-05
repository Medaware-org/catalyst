package org.medaware.catalyst.persistence.model

import com.google.gson.InstanceCreator
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "render_task")
class RenderTaskEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @ManyToOne
    @JoinColumn(name = "article_id")
    lateinit var article: ArticleEntity

    @Column(name = "antg_res")
    lateinit var antgResult: String

    @Column(name = "html_res")
    lateinit var htmlResult: String

    @Column(name = "invalidated")
    var invalidated: Boolean = false

    @Column(name = "created_at")
    lateinit var createdAt: Instant
    
}