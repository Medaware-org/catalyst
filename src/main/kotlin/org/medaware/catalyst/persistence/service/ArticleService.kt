package org.medaware.catalyst.persistence.service

import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.IndexWriter
import org.medaware.catalyst.authenticatedUser
import org.medaware.catalyst.error.CatalystException
import org.medaware.catalyst.lucene.LuceneService
import org.medaware.catalyst.model.BriefArticleResponse
import org.medaware.catalyst.persistence.entity.ArticleEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ArticleService(
    val articleRepository: ArticleRepository,

    @Lazy
    val luceneService: LuceneService
) {

    fun createArticle(title: String, lead: String, content: String): UUID {
        val entity = ArticleEntity()
        entity.createdAt = Instant.now()
        entity.title = title
        entity.lead = lead
        entity.content = content
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

    fun search(query: String): List<BriefArticleResponse> {
        return luceneService.search(query, 15).map {
            return@map BriefArticleResponse(it.get("title"), it.get("lead"), UUID.fromString(it.get("id")))
        }
    }

    fun byId(id: UUID): Array<String> {
        val entity = articleRepository.findById(id)

        if (entity.isEmpty)
            throw CatalystException("Not found", "The requested article could not be found", HttpStatus.NOT_FOUND)

        return arrayOf(entity.get().title, entity.get().lead, entity.get().content)
    }

}