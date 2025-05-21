package org.medaware.catalyst.persistence.model

import jakarta.persistence.*
import org.medaware.catalyst.dto.TopicResponse
import java.util.*

@Entity
@Table(name = "topic")
class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @Column(name = "name")
    lateinit var name: String

    @Column(name = "description")
    lateinit var description: String

    @Column(name = "editable")
    var editable: Boolean = true

    @Column(name = "color")
    lateinit var color: String

    @Column(name = "text_color")
    lateinit var textColor: String

    fun toDto(isFallback: Boolean): TopicResponse =
        TopicResponse(
            id = id,
            name = name,
            description = description,
            color = color,
            editable = editable,
            textColor = textColor,
            isFallback = isFallback
        )

}