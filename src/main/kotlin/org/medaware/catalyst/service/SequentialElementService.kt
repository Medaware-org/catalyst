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
        // Since we need linear (sequential) flow, we have to check whether our current item would collide
        // or break a path of another item and handle the situation before creating a new element
        val conflict: SequentialElementEntity? = if (preceding != null) findNext(preceding) else null

        val seq = SequentialElementEntity()
        seq.precedingElement = preceding
        seq.handle = handle
        sequentialElementRepository.save(seq)

        // Relocate the conflicting element if needed
        if (conflict != null) {
            conflict.precedingElement = seq
            sequentialElementRepository.save(conflict)
        }

        return seq
    }

    fun findNext(current: SequentialElementEntity): SequentialElementEntity? =
        sequentialElementRepository.getSequentialElementEntityByPrecedingElement(current)

}