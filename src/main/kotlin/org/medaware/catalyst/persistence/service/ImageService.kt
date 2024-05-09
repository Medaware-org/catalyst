package org.medaware.catalyst.persistence.service

import org.medaware.catalyst.persistence.repository.ImageRepository
import org.springframework.stereotype.Service

@Service
class ImageService (
    val imageRepository: ImageRepository
) {

}