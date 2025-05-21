package org.medaware.catalyst.service

import jakarta.annotation.PostConstruct
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.Term
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.BooleanClause
import org.apache.lucene.search.BooleanQuery
import org.apache.lucene.search.FuzzyQuery
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.hibernate.sql.ast.tree.select.QueryPart
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
    private lateinit var searcher: IndexSearcher
    private lateinit var reader: DirectoryReader

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
        writer.close()
        reader = DirectoryReader.open(index)
        searcher = IndexSearcher(reader)
        logger.info("Index built with $articleCount article(s).");
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
    }

    fun queryArticles(query: String): List<ArticleResponse> {
        val fields = arrayOf("title", "content", "id", "author")
        val parser = MultiFieldQueryParser(fields, analyzer)
        val sanitized = QueryParser.escape(query) + "~"
        val luceneQuery = parser.parse(sanitized)
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

}