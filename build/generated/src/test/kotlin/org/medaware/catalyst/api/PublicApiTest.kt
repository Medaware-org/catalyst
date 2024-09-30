package org.medaware.catalyst.api

import org.medaware.catalyst.dto.BriefArticleResponse
import org.medaware.catalyst.dto.CatalystCreateArticleRequest
import org.medaware.catalyst.dto.CatalystError
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class PublicApiTest {

    private val api: PublicApiController = PublicApiController()

    /**
     * To test PublicApiController.catalystGetAllArticles
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun catalystGetAllArticlesTest() {
        val response: ResponseEntity<List<BriefArticleResponse>> = api.catalystGetAllArticles()

        // TODO: test validations
    }

    /**
     * To test PublicApiController.catalystGetArticle
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun catalystGetArticleTest() {
        val id: java.util.UUID = TODO()
        val response: ResponseEntity<CatalystCreateArticleRequest> = api.catalystGetArticle(id)

        // TODO: test validations
    }

    /**
     * To test PublicApiController.catalystQueryArticle
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun catalystQueryArticleTest() {
        val query: kotlin.String = TODO()
        val response: ResponseEntity<List<BriefArticleResponse>> = api.catalystQueryArticle(query)

        // TODO: test validations
    }
}
