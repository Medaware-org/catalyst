package org.medaware.catalyst.rest

import org.medaware.catalyst.api.PublicApi
import org.medaware.catalyst.dto.*
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.service.ArticleService
import org.medaware.catalyst.service.LuceneService
import org.medaware.catalyst.service.OCRService
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PublicController(
    val luceneService: LuceneService,
    val articleService: ArticleService,
    val ocrService: OCRService
) : PublicApi {

    override fun queryArticles(query: String): ResponseEntity<List<ArticleResponse>> =
        ResponseEntity.ok(luceneService.queryArticles(query))

    override fun getAllArticles(): ResponseEntity<List<ArticleResponse>> =
        ResponseEntity.ok(articleService.getAllArticles().map { it.toDto() })


    private fun searchUniqueSet(arr: List<String>): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        arr.forEach {
            val articles = luceneService.queryArticles(it)
            articles.forEach { list.add(it.title to "http://google.com") }
        }
        return list
    }

    override fun requestOCR(file: Resource): ResponseEntity<RequestOCR200Response> {
        val ocrResult = ocrService.requestOcr(
            file.filename ?: throw CatalystException(
                "No Filename", "Could not request OCR: No filename given",
                HttpStatus.BAD_REQUEST
            ), file.contentAsByteArray
        ).data

        return ResponseEntity.ok(
            RequestOCR200Response(
                data = ocrResult,
                articles = searchUniqueSet(ocrResult).map { RequestOCR200ResponseArticlesInner(it.first, it.second) }
            )
        )
    }

    override fun requestGHS(file: Resource): ResponseEntity<RequestGHS200Response> {
        val response = ocrService.requestGhs(
            file.filename ?: throw CatalystException(
                "No Filename", "Could not request OCR: No filename given",
                HttpStatus.BAD_REQUEST
            ), file.contentAsByteArray
        )

        return ResponseEntity.ok(
            RequestGHS200Response(
                response.output.first(),
                response.scores.map {
                    RequestGHS200ResponseScoresInner(it.key, it.value)
                }
            )
        )
    }
}