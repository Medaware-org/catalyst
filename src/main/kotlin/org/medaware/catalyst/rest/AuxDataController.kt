package org.medaware.catalyst.rest

import org.medaware.catalyst.api.TangentialAuxiliaryApi
import org.medaware.catalyst.dto.ElementTypeRequirement
import org.medaware.catalyst.service.MetadataService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuxDataController(
    val metadataService: MetadataService,
) : TangentialAuxiliaryApi {

    override fun getElementTypes(): ResponseEntity<List<ElementTypeRequirement>> {
        return ResponseEntity.ok(metadataService.getAvailableElementTypesAndMetaRequirements())
    }

    override fun getMetaEntryValueConstraints(type: String): ResponseEntity<List<String>> {
        return ResponseEntity.ok((metadataService.getValueConstraintsOf(type).let {
            if (it == null)
                return@let listOf<String>()
            else
                return@let it.asList()
        }))
    }
}