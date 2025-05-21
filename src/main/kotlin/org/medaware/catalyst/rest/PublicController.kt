package org.medaware.catalyst.rest

import org.medaware.catalyst.api.PublicApi
import org.medaware.catalyst.dto.ArticleResponse
import org.medaware.catalyst.dto.RequestGHS200Response
import org.medaware.catalyst.dto.RequestGHS200ResponseScoresInner
import org.medaware.catalyst.dto.RequestOCR200Response
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

    
    override fun requestOCR(file: Resource): ResponseEntity<RequestOCR200Response> =
        ResponseEntity.ok(
            RequestOCR200Response(
                ocrService.requestOcr(
                    file.filename ?: throw CatalystException(
                        "No Filename", "Could not request OCR: No filename given",
                        HttpStatus.BAD_REQUEST
                    ), file.contentAsByteArray
                ).data
            )
        )

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