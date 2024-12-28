package org.medaware.catalyst.rest

import org.medaware.catalyst.api.PublicApi
import org.medaware.catalyst.dto.QueryResponse
import org.medaware.catalyst.service.LuceneService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PublicController(
    val luceneService: LuceneService
) : PublicApi {

    override fun queryArticles(query: String): ResponseEntity<List<QueryResponse>> {
        return ResponseEntity.ok(luceneService.queryArticles(query))
    }

}