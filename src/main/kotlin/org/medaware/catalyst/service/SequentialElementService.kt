package org.medaware.catalyst.service

import org.medaware.anterogradia.rootCause
import org.medaware.avis.AvisMeta
import org.medaware.avis.exception.AvisValidationException
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.medaware.catalyst.persistence.repository.SequentialElementRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.UUID

@Service

class SequentialElementService(
    val sequentialElementRepository: SequentialElementRepository,
    val metadataService: MetadataService,
    val articleRepository: ArticleRepository
) {

    fun getById(id: UUID): SequentialElementEntity? =
        sequentialElementRepository.getSequentialElementEntityById(id)

    fun getByArticleAndId(article: ArticleEntity, id: UUID): SequentialElementEntity? =
        sequentialElementRepository.getSequentialElementEntityByArticleAndId(article, id)

    fun getByArticle(article: ArticleEntity): List<SequentialElementEntity> =
        sequentialElementRepository.getSequentialElementEntitiesByArticle(article)

    fun getByArticleAndHandle(article: ArticleEntity, handle: String): SequentialElementEntity? =
        sequentialElementRepository.getSequentialElementEntityByArticleAndHandle(article, handle)

    fun putMeta(element: SequentialElementEntity, meta: Pair<String, String>) {
        metadataService.putMetaEntry(element, meta.first, meta.second)
    }

    fun createSequentialElement(
        article: ArticleEntity,
        handle: String,
        preceding: SequentialElementEntity? = null
    ): SequentialElementEntity {
        if (!handle.matches("[a-zA-Z0-9\\-]+".toRegex()))
            throw CatalystException(
                "Invalid Handle",
                "The handle '$handle' does not match the required form",
                HttpStatus.UNPROCESSABLE_ENTITY
            )

        if (getByArticleAndHandle(article, handle) != null)
            throw CatalystException(
                "Handle Conflict",
                "An element with this handle is already present in this article",
                HttpStatus.CONFLICT
            )

        // Since we need linear (sequential) flow, we have to check whether our current item would collide
        // or break a path of another item and handle the situation before creating a new element
        val conflict: SequentialElementEntity? = if (preceding != null) findNext(preceding) else null

        val seq = SequentialElementEntity()
        seq.precedingElement = preceding
        seq.handle = handle
        seq.article = article
        sequentialElementRepository.save(seq)

        // Relocate the conflicting element if needed
        if (conflict != null) {
            conflict.precedingElement = seq
            sequentialElementRepository.save(conflict)
        }

        // Handle insertion before root
        if (preceding == null && article.rootElement != null) {
            val rootElement = getById(article.rootElement!!)!!
            rootElement.precedingElement = seq
            article.rootElement = seq.id

            articleRepository.save(article)
            sequentialElementRepository.save(rootElement)
        }

        return seq
    }

    private fun createTypedSequentialElement(
        article: ArticleEntity,
        handle: String,
        preceding: SequentialElementEntity?,
        type: String
    ): SequentialElementEntity {
        val element = createSequentialElement(article, handle, preceding)
        putMeta(element, AvisMeta.ELEMENT_TYPE.toString() to type)
        return element
    }

    fun insertElement(
        article: ArticleEntity,
        after: UUID?,
        handle: String,
        type: String = "BLANK_PLACEHOLDER",
        meta: Array<Pair<String, String>> = arrayOf()
    ): SequentialElementEntity {
        val preceding: SequentialElementEntity? = if (after == null) null else getByArticleAndId(article, after)

        if (after != null && preceding == null)
            throw CatalystException(
                "Constraint Error",
                "The supposed preceding element '$after' does not exist",
                HttpStatus.NOT_FOUND
            )

        val element = createTypedSequentialElement(article, handle, preceding, type)

        meta.forEach {
            metadataService.putMetaEntry(element, it.first, it.second)
        }

        return element
    }

    fun findNext(current: SequentialElementEntity): SequentialElementEntity? =
        sequentialElementRepository.getSequentialElementEntityByPrecedingElementAndArticle(current, current.article)

    fun findAllElementsOfArticle(article: ArticleEntity): List<SequentialElementEntity> {
        val elements = mutableListOf<SequentialElementEntity>()

        var element = getByArticleAndId(article, article.rootElement ?: return listOf())

        while (element != null) {
            elements.add(element)
            element = findNext(element)
        }

        return elements
    }

    fun putElementTypeAndRequiredMetas(element: SequentialElementEntity, type: String, substituteAll: Boolean = false) {
        if (substituteAll) {
            metadataService.dropAllMetaOf(element)
            metadataService.putMetaEntry(element, AvisMeta.ELEMENT_TYPE.toString(), type.uppercase())
        }
        try {
            AvisMeta.use(AvisMeta.ELEMENT_TYPE.toString(), type) { meta, requirements ->
                requirements.forEach {
                    val meta = AvisMeta.byNameOrNull(it) ?: throw CatalystException(
                        "Unknown Requirement",
                        "Could not find the required meta entry '${
                            it.toString().uppercase()
                        }' for element type '${type}'",
                        HttpStatus.NOT_FOUND
                    )
                    metadataService.putMetaEntry(element, meta.toString().uppercase(), meta.defaultValue() ?: "")
                }
            }
        } catch (e: AvisValidationException) {
            throw CatalystException("AVIS Validation Failed", e.message, HttpStatus.UNPROCESSABLE_ENTITY)
        }
    }

    fun getAvailableElementTypes(): Array<String> {
        return AvisMeta.ELEMENT_TYPE.valueConstraints!!
    }

    /**
     * Removes all metadata from the given elements and inserts metas for the requested type
     */
    fun switchElementToType(element: SequentialElementEntity, type: String) {
        val upperType = type.uppercase()

        val exception = AvisMeta.validateMetaEntry(upperType, AvisMeta.ELEMENT_TYPE.toString() to upperType)

        if (exception != null)
            throw CatalystException(
                "Metadata Validation Failed",
                exception.rootCause().message ?: "No further details",
                HttpStatus.UNPROCESSABLE_ENTITY
            )

        putElementTypeAndRequiredMetas(element, type, substituteAll = true)
    }


    fun deleteElement(element: SequentialElementEntity, allowRootDeletion: Boolean = false) {
        val preceding = element.precedingElement ?: (if (!allowRootDeletion) throw CatalystException(
            "Inappropriate Deletion",
            "The root element of an article cannot be deleted",
            HttpStatus.CONFLICT
        ) else null)

        val following = findNext(element)

        // If there is a following element, "re-wire" it to the preceding entry
        if (following != null && preceding != null) {
            following.precedingElement = preceding
            sequentialElementRepository.save(following)
        }

        metadataService.dropAllMetaOf(element)

        sequentialElementRepository.delete(element)
    }

}