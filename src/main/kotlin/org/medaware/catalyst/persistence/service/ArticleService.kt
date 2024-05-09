package org.medaware.catalyst.persistence.service

import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.IndexWriter
import org.medaware.catalyst.authenticatedUser
import org.medaware.catalyst.lucene.LuceneService
import org.medaware.catalyst.model.ArticleCreationRequest
import org.medaware.catalyst.model.ArticleQueryRequest
import org.medaware.catalyst.model.BriefArticleResponse
import org.medaware.catalyst.persistence.entity.ArticleEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ArticleService(
    val articleRepository: ArticleRepository,

    @Lazy
    val luceneService: LuceneService
) {

    fun createArticle(req: ArticleCreationRequest): UUID {
        val entity = ArticleEntity()
        entity.createdAt = Instant.now()
        entity.title = req.title
        entity.lead = req.lead
        entity.content = req.content
        entity.createdby = authenticatedUser().id

        articleRepository.save(entity)

        luceneService.appendIndex(entity)

        return entity.id
    }

    fun allArticles(): List<ArticleEntity> {
        return articleRepository.findAll()
    }

    fun luceneDocument(writer: IndexWriter, entity: ArticleEntity): Document {
        val doc = Document()
        doc.add(TextField("title", entity.title, Field.Store.YES))
        doc.add(TextField("lead", entity.lead, Field.Store.YES))
        doc.add(TextField("content", entity.content, Field.Store.YES))
        doc.add(StringField("id", entity.id.toString(), Field.Store.YES))
        return doc
    }

    fun search(articleQueryRequest: ArticleQueryRequest): List<BriefArticleResponse> {
        return luceneService.search(articleQueryRequest.query, articleQueryRequest.hitsCount).map {
            return@map BriefArticleResponse(it.get("title"), it.get("lead"), UUID.fromString(it.get("id")))
        }
    }

}