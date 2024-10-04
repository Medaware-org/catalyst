package org.medaware.catalyst.service

import jakarta.transaction.Transactional
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.medaware.catalyst.persistence.repository.SequentialElementRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class SequentialElementService(
    val sequentialElementRepository: SequentialElementRepository
) {

    fun getById(id: UUID) = sequentialElementRepository.getSequentialElementEntityById(id)

    fun createSequentialElement(handle: String, preceding: SequentialElementEntity? = null): SequentialElementEntity {
        val seq = SequentialElementEntity()
        seq.precedingElement = preceding
        seq.handle = handle
        sequentialElementRepository.save(seq)
        return seq
    }

}