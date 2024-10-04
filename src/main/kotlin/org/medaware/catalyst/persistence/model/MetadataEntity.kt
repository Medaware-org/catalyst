package org.medaware.catalyst.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.medaware.catalyst.dto.MetadataEntry
import java.util.UUID

@Entity
@Table(name = "element_metadata")
class MetadataEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @ManyToOne
    @JoinColumn(name = "element")
    lateinit var element: SequentialElementEntity

    @Column(name = "meta_key")
    lateinit var key: String

    @Column(name = "meta_value")
    lateinit var value: String

    fun toDto() =
        MetadataEntry(key, value, id)

}