package org.medaware.catalyst.api

import org.medaware.catalyst.dto.UuidResponse
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class ArticleApiTest {

    private val api: ArticleApiController = ArticleApiController()

    /**
     * To test ArticleApiController.catalystCreateArticle
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    fun catalystCreateArticleTest() {
        val title: kotlin.String = TODO()
        val lead: kotlin.String = TODO()
        val html: kotlin.String = TODO()
        val response: ResponseEntity<UuidResponse> = api.catalystCreateArticle(title, lead, html)

        // TODO: test validations
    }
}
