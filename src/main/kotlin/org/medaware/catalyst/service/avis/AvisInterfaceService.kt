package org.medaware.catalyst.service.avis

import org.medaware.anterogradia.Anterogradia
import org.medaware.anterogradia.rootCause
import org.medaware.anterogradia.runtime.Runtime
import org.medaware.avis.MedawareDesignKit
import org.medaware.avis.model.AvisArticle
import org.medaware.avis.model.AvisElement
import org.medaware.avis.render.renderer
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.persistence.model.SequentialElementEntity
import org.medaware.catalyst.service.MetadataService
import org.medaware.catalyst.service.SequentialElementService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

data class RenderResultObject(val antg: String, val html: String)

@Service
class AvisInterfaceService(
    val elementService: SequentialElementService,
    val metadataService: MetadataService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun render(avisArticle: AvisArticle): RenderResultObject {
        val antgSource = try {
            avisArticle.renderer().render()
        } catch (e: Exception) {
            e.printStackTrace()
            throw CatalystException(
                "Rendering Failed",
                "Failed to render article '${avisArticle.id}' via AVIS. Please contact the webmaster.",
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        val dumpedTree = antgSource.dump()

        val runtime = Runtime()
        runtime.loadLibrary(MedawareDesignKit::class.java.canonicalName, "avis")

        var htmlResult: String =
            Anterogradia.invokeCompiler(dumpedTree, antgRuntime = runtime).use { input, output, except, dump ->
                if (except != null) {
                    val rootCause = except.rootCause()
                    logger.error("Could not render Anterogradia sources for article ${avisArticle.id} !!")
                    logger.error(rootCause::class.simpleName + " :: " + rootCause.message)
                    logger.error(input)
                    throw CatalystException(
                        "Rendering Error",
                        "Could not compile generated Anterogradia sources. This should not happen! Please contact the webmaster!",
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
                }
                return@use output
            }

        return RenderResultObject(dumpedTree, htmlResult)
    }

    fun assembleArticle(article: ArticleEntity): AvisArticle {
        val elements = mutableListOf<AvisElement>()

        var element: SequentialElementEntity? =
            elementService.getById(article.rootElement ?: return AvisArticle(article.id, article.title, listOf()))

        while (element != null) {
            elements.add(AvisElement(element.id, element.handle, collectMetadata(element)))

            // Now, we need to find the element that refers back to the current element as its preceding item
            element = elementService.findNext(element)
        }

        return AvisArticle(article.id, article.title, elements)
    }

    private fun collectMetadata(element: SequentialElementEntity): HashMap<String, String> =
        metadataService.getMetadataMapOf(element)

}