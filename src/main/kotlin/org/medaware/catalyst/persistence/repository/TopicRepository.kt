package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.model.TopicEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TopicRepository : JpaRepository<TopicEntity, UUID> {

    fun getByNameAndDescription(name: String, description: String): TopicEntity?

}