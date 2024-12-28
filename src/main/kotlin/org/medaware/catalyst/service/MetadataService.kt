package org.medaware.catalyst.service

import org.medaware.avis.AvisMeta
import org.medaware.catalyst.dto.ElementTypeRequirement
import org.medaware.catalyst.dto.MetadataEntry
import org.medaware.catalyst.persistence.model.MetadataEntity
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.medaware.catalyst.persistence.repository.MetadataRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service

class MetadataService(
    val metadataRepository: MetadataRepository
) {

    fun getById(id: UUID) = metadataRepository.findById(id)

    fun getByKeyAndElement(key: String, element: SequentialElementEntity) =
        metadataRepository.getMetadataEntityByKeyAndElement(key, element)

    fun getMetadataOf(element: SequentialElementEntity): List<MetadataEntity> =
        metadataRepository.getMetadataEntitiesByElement(element)

    fun getMetadataMapOf(element: SequentialElementEntity): HashMap<String, String> =
        HashMap(getMetadataOf(element).associate { it.key to it.value })

    fun putMetaEntry(element: SequentialElementEntity, key: String, value: String): MetadataEntity {
        val conflict = getByKeyAndElement(key, element)

        if (conflict != null) {
            conflict.value = value
            metadataRepository.save(conflict)
            return conflict
        }

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

    fun getAvailableElementTypesAndMetaRequirements(): List<ElementTypeRequirement> {
        val requirements = mutableListOf<ElementTypeRequirement>()
        AvisMeta.ELEMENT_TYPE.valueConstraints!!.forEach {
            AvisMeta.use(AvisMeta.ELEMENT_TYPE.toString(), it) { meta, required ->
                requirements.add(ElementTypeRequirement(it, required.asList()))
            }
        }
        return requirements
    }

    fun getValueConstraintsOfAllMetadataEntries(): List<Pair<String, Array<String>>> {
        val constraints = mutableListOf<Pair<String, Array<String>>>()
        AvisMeta.entries.toTypedArray().forEach {
            constraints.add(it.toString() to (it.valueConstraints ?: arrayOf()))
        }
        return constraints
    }

    fun getValueConstraintsOf(entryType: String): Array<String>? {
        val meta = AvisMeta.byNameOrNull(entryType) ?: return null
        return meta.valueConstraints
    }

}