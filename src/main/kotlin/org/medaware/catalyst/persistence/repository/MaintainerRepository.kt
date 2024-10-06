package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MaintainerRepository : JpaRepository<MaintainerEntity, UUID> {

    fun getMaintainerEntityById(id: UUID): MaintainerEntity?

    fun getMaintainerEntityByUsername(username: String): MaintainerEntity?

}