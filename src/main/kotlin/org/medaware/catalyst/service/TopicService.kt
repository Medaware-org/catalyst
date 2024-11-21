package org.medaware.catalyst.service

import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.medaware.catalyst.config.DefaultTopicConfig
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.persistence.model.TopicEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.medaware.catalyst.persistence.repository.TopicRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class TopicService(
    val topicRepository: TopicRepository,
    val defaultTopicConfig: DefaultTopicConfig,
    val articleRepository: ArticleRepository
) {

    private var fallbackTopic: TopicEntity? = null

    private var logger = LoggerFactory.getLogger(TopicService::class.java)

    fun getFallbackTopic() = fallbackTopic!!

    @PostConstruct
    @Transactional
    fun createFallbackTopic() {
        fallbackTopic = findTopic(defaultTopicConfig.name, defaultTopicConfig.description) ?: run {
            logger.info("Create fallback topic \"${defaultTopicConfig.name}\".\" (\"${defaultTopicConfig.description}\")")
            return@run createTopic(
                defaultTopicConfig.name,
                defaultTopicConfig.description,
                editable = false
            )
        }

        if (fallbackTopic!!.editable)
            throw IllegalStateException("The default topic \"${defaultTopicConfig.name}\" (\"${defaultTopicConfig.description}\") must not be editable!")
    }

    fun createTopic(name: String, description: String, editable: Boolean = true): TopicEntity {
        val entity = TopicEntity()
        entity.name = name
        entity.description = description
        entity.editable = editable
        topicRepository.save(entity)

        return entity
    }

    fun findTopic(name: String, description: String): TopicEntity? =
        topicRepository.getByNameAndDescription(name, description)

    fun getAllTopics(): List<TopicEntity> = topicRepository.findAll()

    private fun retrieveTopic(id: UUID): TopicEntity = topicRepository.findById(id).orElseThrow {
        CatalystException(
            "Unknown Topic",
            "Could not modify undefined topic $id",
            HttpStatus.NOT_FOUND
        )
    }

    fun updateTopic(id: UUID, name: String?, description: String?) {
        val entity = retrieveTopic(id)

        if (name != null)
            entity.name = name

        if (description != null)
            entity.description = description

        topicRepository.save(entity)
    }

    private fun removeTopicEntity(topic: TopicEntity) {
        topicRepository.delete(topic);
    }

    fun removeTopic(id: UUID, fallback: TopicEntity = this.fallbackTopic!!) {
        val topic = retrieveTopic(id)
        articleRepository.getArticleEntitiesByTopic(topic).forEach { article ->
            article.topic = fallback
            articleRepository.save(article)
        }
        removeTopicEntity(topic);
    }

}