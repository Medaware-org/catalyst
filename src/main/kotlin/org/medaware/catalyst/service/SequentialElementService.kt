package org.medaware.catalyst.service

import jakarta.transaction.Transactional
import org.medaware.avis.AvisMeta
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.medaware.catalyst.persistence.repository.SequentialElementRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class SequentialElementService(
    val sequentialElementRepository: SequentialElementRepository,
    val metadataService: MetadataService
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

    fun insertBlankElement(after: UUID, handle: String): SequentialElementEntity {
        val preceding = sequentialElementRepository.findById(after)

        if (!preceding.isPresent)
            throw CatalystException(
                "Constraint Error",
                "The supposed preceding element '$after' does not exist",
                HttpStatus.NOT_FOUND
            )

        val element = createSequentialElement(handle, preceding.get())

        metadataService.putMetaEntry(element, AvisMeta.ELEMENT_TYPE.toString(), "BLANK_PLACEHOLDER")

        return element
    }

    fun findNext(current: SequentialElementEntity): SequentialElementEntity? =
        sequentialElementRepository.getSequentialElementEntityByPrecedingElement(current)

    fun findAllElementsOfArticle(article: ArticleEntity): List<SequentialElementEntity> {
        val elements = mutableListOf<SequentialElementEntity>()

        var element = getById(article.rootElement ?: return listOf())

        while (element != null) {
            elements.add(element)
            element = findNext(element)
        }

        return elements
    }

}