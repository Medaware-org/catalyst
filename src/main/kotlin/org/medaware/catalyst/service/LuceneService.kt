package org.medaware.catalyst.service

import jakarta.annotation.PostConstruct
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.medaware.catalyst.dto.ArticleResponse
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.util.*

@Service
class LuceneService(
    val articleService: ArticleService
) {

    private lateinit var index: Directory
    private lateinit var analyzer: StandardAnalyzer
    private lateinit var writerConfig: IndexWriterConfig
    private lateinit var writer: IndexWriter

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun config() {
        index = RAMDirectory()
        analyzer = StandardAnalyzer()
        writerConfig = IndexWriterConfig(analyzer)
        writer = IndexWriter(index, writerConfig)
        buildInitialIndex()
    }

    fun buildInitialIndex() {
        logger.info("Building article index ...");
        var articleCount = 0;
        articleService.getAllArticles().forEach {
            indexArticle(it)
            articleCount++
        }
        if (articleCount == 0)
            return;

        logger.info("Index built with $articleCount article(s).")
    }

    fun indexArticle(article: ArticleEntity) {
        val document = Document()
        val content = articleService.dumpToString(article)
        val title = article.title
        document.add(TextField("title", title, Field.Store.YES))
        document.add(TextField("content", content, Field.Store.YES))
        document.add(TextField("id", article.id.toString(), Field.Store.YES))
        document.add(TextField("author", article.maintainer.username, Field.Store.YES))
        document.add(TextField("authorId", article.maintainer.id.toString(), Field.Store.YES))
        document.add(TextField("topic", article.topic.name.lowercase(), Field.Store.YES))
        document.add(TextField("date", article.createdAt.toString(), Field.Store.YES))
        writer.addDocument(document)
        writer.commit()
    }

    fun queryArticles(query: String): List<ArticleResponse> {
        writer.deleteAll()

//        TODO Uncomment this
//        buildInitialIndex()

        // Query too short to perform a reasoanble fuzzy search
        if (query.length < 3)
            return articleService.getAllArticles().map { it.toDto() }

        val fields = arrayOf("title", "content", "id", "author")
        val parser = MultiFieldQueryParser(fields, analyzer)
        val sanitized = QueryParser.escape(query) + "~"
        val luceneQuery = parser.parse(sanitized)
        val reader = DirectoryReader.open(index)
        val searcher = IndexSearcher(reader)
        val tops = searcher.search(luceneQuery, 100)
        val responses: MutableList<ArticleResponse> = mutableListOf()
        tops.scoreDocs.forEach {
            val document = searcher.doc(it.doc)
            responses.add(
                ArticleResponse(
                    document.get("author"),
                    document.get("title"),
                    Instant.parse(document.get("date")).atZone(ZoneId.systemDefault()).toLocalDate(),
                    UUID.fromString(document.get("authorId")),
                    UUID.fromString(document.get("id")),
                )
            )
        }
        return responses
    }

//    fun queryUnique(queries: List<String>): List<ArticleResponse> {
//        val results = mutableListOf<ArticleResponse>()
//        queries.forEach {
//            TODO()
//        }
//    }

}