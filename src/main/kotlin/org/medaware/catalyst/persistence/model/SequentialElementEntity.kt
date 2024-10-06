package org.medaware.catalyst.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.medaware.catalyst.dto.ElementResponse
import java.util.UUID

@Entity
@Table(name = "sequential_element")
class SequentialElementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    lateinit var id: UUID

    @Column(name = "handle")
    lateinit var handle: String

    @ManyToOne
    @JoinColumn(name = "preceding_element")
    var precedingElement: SequentialElementEntity? = null

    @ManyToOne
    @JoinColumn(name = "article")
    lateinit var article: ArticleEntity

    fun toDto() =
        ElementResponse(id, handle)

}