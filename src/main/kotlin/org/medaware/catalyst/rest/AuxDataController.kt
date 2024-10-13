package org.medaware.catalyst.rest

import org.medaware.catalyst.api.TangentialAuxiliaryApi
import org.medaware.catalyst.dto.ElementTypeRequirement
import org.medaware.catalyst.dto.GetMetaEntryValueConstraints200ResponseInner
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

    override fun getMetaEntryValueConstraints(): ResponseEntity<List<GetMetaEntryValueConstraints200ResponseInner>> {
        return ResponseEntity.ok(metadataService.getValueConstraintsOfAllMetadataEntries().map {
            GetMetaEntryValueConstraints200ResponseInner(it.first, it.second.toList())
        })
    }

}