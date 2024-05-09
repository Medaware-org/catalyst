package org.medaware.catalyst.persistence.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(schema = "medaware", name = "image")
class ImageEntity {

    @Id
    @GeneratedValue
    lateinit var id: UUID

    @Column(name = "unique_designator")
    lateinit var uniqueDesignator: String

}