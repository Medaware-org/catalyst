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
import org.apache.lucene.search.BooleanClause
import org.apache.lucene.search.BooleanQuery
import org.apache.lucene.search.FuzzyQuery
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.medaware.catalyst.dto.QueryResponse
import org.medaware.catalyst.persistence.model.ArticleEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
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
        writer.addDocument(document)
    }

    fun queryArticles(query: String): List<QueryResponse> {
        val fields = arrayOf("title", "content", "id", "author")
        val booleanQuery = BooleanQuery.Builder()
        fields.forEach {
            val fuzzy = FuzzyQuery(Term(it, query))
            booleanQuery.add(fuzzy, BooleanClause.Occur.SHOULD);
        }
        val query = booleanQuery.build()
        val tops = searcher.search(query, 100)
        val responses: MutableList<QueryResponse> = mutableListOf()
        tops.scoreDocs.forEach {
            val document = searcher.doc(it.doc)
            responses.add(
                QueryResponse(
                    UUID.fromString(document.get("id")),
                    document.get("title"),
                    document.get("author")
                )
            )
        }
        return responses
    }

}