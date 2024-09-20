package org.medaware.catalyst.lucene

import jakarta.annotation.PostConstruct
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.Directory
import org.medaware.catalyst.persistence.entity.ArticleEntity
import org.medaware.catalyst.persistence.service.ArticleService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class LuceneService(
    val articleService: ArticleService,
    val index: Directory,
    val writerConfig: IndexWriterConfig,
    val luceneAnalyzer: StandardAnalyzer,
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val documentBuffer: MutableList<ArticleEntity> = mutableListOf()

    private lateinit var writer: IndexWriter

    @PostConstruct
    @Order(HIGHEST_PRECEDENCE)
    private fun init() {
        this.writer = IndexWriter(index, writerConfig)
        logger.info("Lucene service initialized")
    }

    @PostConstruct
    @Order(LOWEST_PRECEDENCE)
    private fun indexArticles() {
        logger.info("Building Lucene index ...")

        val articles = articleService.allArticles()

        articles.forEach {
            appendIndex(it)
        }

        logger.info("Index built with ${articles.size} articles.")

        writer.commit()
    }

    @Scheduled(fixedRate = 1000*60*10)
    fun flushIndex() {
        writer.addDocuments(this.documentBuffer.map { articleService.luceneDocument(writer, it) })
        writer.commit()
    }

    fun appendIndex(article: ArticleEntity) {
        this.documentBuffer.add(article)

        if(this.documentBuffer.size > 20) {
            this.flushIndex()
        }

    }

    fun search(queryStr: String, hits: Int): List<Document> {
        val query = MultiFieldQueryParser(arrayOf("title", "lead", "content"), luceneAnalyzer).parse("${queryStr}~")

        val reader = DirectoryReader.open(index)
        val storedFields = reader.storedFields()
        val searcher = IndexSearcher(reader)
        val docs = searcher.search(query, hits)

        return docs.scoreDocs.map {
            return@map storedFields.document(it.doc)
        }
    }

}