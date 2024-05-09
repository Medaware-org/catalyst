package org.medaware.catalyst.persistence.repository

import org.medaware.catalyst.persistence.entity.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ImageRepository : JpaRepository<ImageEntity, UUID>