package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.MetadataEntity
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MetadataRepository : JpaRepository<MetadataEntity, UUID> {

    fun getMetadataEntitiesByElement(element: SequentialElementEntity): List<MetadataEntity>

    fun getMetadataEntityByKeyAndElement(key: String, element: SequentialElementEntity): MetadataEntity?

}