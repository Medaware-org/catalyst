package org.medaware.catalyst

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.medaware.catalyst.persistence.model.Article
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.medaware.catalyst.service.LuceneService
import org.medaware.catalyst.service.MaintainerService
import org.medaware.catalyst.service.TopicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*
import java.util.logging.Logger

@SpringBootTest
class LuceneTests {

    @Autowired
    private lateinit var luceneService: LuceneService

    @Autowired
    private lateinit var maintainerService: MaintainerService

    @Autowired
    private lateinit var topicService: TopicService

    private lateinit var testingArticle: ArticleEntity

    @BeforeEach
    fun createTestingArticle() {
        luceneService.config()
        luceneService.buildInitialIndex()

        testingArticle = Article {
            id = UUID.randomUUID()
            maintainer = maintainerService.getDefault()
            title = "Hello World"
            createdAt = Instant.now()
            rootElement = null
            visible = true
            topic = topicService.getFallbackTopic()
        }

        luceneService.indexArticle(testingArticle)
    }

    @Test
    @DisplayName("Test Lucene Queries (70% Match)")
    fun testLuceneQuery() {
        val article = luceneService.queryArticles("Hello Warlt").find { it.id == testingArticle.id }
        assert(article != null)
        assert(article!!.id == testingArticle.id)
        Logger.getLogger(javaClass.simpleName).info(article.title)
    }

    @Test
    @DisplayName("Test Lucene Queries (100% Match)")
    fun testLuceneQuery2() {
        val article = luceneService.queryArticles("Hello World").find { it.id == testingArticle.id }
        assert(article != null)
        assert(article!!.id == testingArticle.id)
        Logger.getLogger(javaClass.simpleName).info(article.title)
    }

}
