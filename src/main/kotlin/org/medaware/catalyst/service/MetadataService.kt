package org.medaware.catalyst.service

import org.medaware.catalyst.dto.MetadataEntry
import org.medaware.catalyst.persistence.model.MetadataEntity
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.medaware.catalyst.persistence.repository.MetadataRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(propagation = Propagation.REQUIRED)
class MetadataService(
    val metadataRepository: MetadataRepository
) {

    fun getById(id: UUID) = metadataRepository.findById(id)

    fun getMetadataOf(element: SequentialElementEntity): List<MetadataEntity> =
        metadataRepository.getMetadataEntitiesByElement(element)

    fun getMetadataMapOf(element: SequentialElementEntity): HashMap<String, String> =
        HashMap(getMetadataOf(element).map { it.key to it.value }.toMap<String, String>())

    fun putMetaEntry(element: SequentialElementEntity, key: String, value: String): MetadataEntity {
        val entity = MetadataEntity()
        entity.key = key
        entity.value = value
        entity.element = element
        metadataRepository.save(entity)
        return entity
    }

    fun getMetadataAsDtosOf(element: SequentialElementEntity): List<MetadataEntry> =
        getMetadataOf(element).map { MetadataEntry(it.key, it.value, it.id) }

    fun dropAllMetaOf(element: SequentialElementEntity) =
        metadataRepository.deleteAll(getMetadataOf(element))

}