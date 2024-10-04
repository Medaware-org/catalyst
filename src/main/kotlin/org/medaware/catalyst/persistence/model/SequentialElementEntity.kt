package org.medaware.catalyst.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "sequential_element")
class SequentialElementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    lateinit var id: UUID

    @Column(name = "handle")
    lateinit var handle: String

    @ManyToOne
    @JoinColumn(name = "preceding_element")
    var precedingElement: SequentialElementEntity? = null

}