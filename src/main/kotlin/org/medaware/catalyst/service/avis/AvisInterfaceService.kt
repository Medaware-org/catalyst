package org.medaware.catalyst.service.avis

import org.medaware.avis.model.AvisArticle
import org.medaware.avis.model.AvisElement
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.medaware.catalyst.service.ArticleService
import org.medaware.catalyst.service.MetadataService
import org.medaware.catalyst.service.SequentialElementService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AvisInterfaceService(
    val articleService: ArticleService,
    val elementService: SequentialElementService,
    val metadataService: MetadataService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun assembleArticle(article: ArticleEntity): AvisArticle {
        val elements = mutableListOf<AvisElement>()

        var element: SequentialElementEntity? = elementService.getById(article.rootElement)

        if (element == null) {
            logger.warn("The Article \"${article.title}\" (${article.id}) does not have a root element. Is it meant to be this way?")
            return AvisArticle(elements)
        }

        while (element != null) {
            elements.add(AvisElement(collectMetadata(element)))

            // Now, we need to find the element that refers back to the current element as its preceding item
            element = elementService.findNext(element)
        }

        return AvisArticle(elements)
    }

    private fun collectMetadata(element: SequentialElementEntity): HashMap<String, String> =
        metadataService.getMetadataMapOf(element)

}