package org.medaware.catalyst.service

import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.medaware.catalyst.config.DefaultTopicConfig
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.persistence.model.TopicEntity
import org.medaware.catalyst.persistence.repository.ArticleRepository
import org.medaware.catalyst.persistence.repository.TopicRepository
import org.medaware.catalyst.textColor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.*

@Service
class TopicService(
    val topicRepository: TopicRepository,
    val defaultTopicConfig: DefaultTopicConfig,
    val articleRepository: ArticleRepository
) {

    private lateinit var fallbackTopic: TopicEntity

    private var logger = LoggerFactory.getLogger(TopicService::class.java)

    fun getFallbackTopic() = fallbackTopic

    @PostConstruct
    @Transactional
    fun createFallbackTopic() {
        fallbackTopic = findTopic(defaultTopicConfig.name, defaultTopicConfig.description) ?: run {
            logger.info("Create fallback topic \"${defaultTopicConfig.name}\".\" (\"${defaultTopicConfig.description}\")")
            return@run createTopic(
                defaultTopicConfig.name,
                defaultTopicConfig.description,
                defaultTopicConfig.color,
                editable = false
            )
        }

        logger.info("Fallback topic is \"${fallbackTopic.name}\"")

        if (fallbackTopic.editable)
            throw IllegalStateException("The default topic \"${defaultTopicConfig.name}\" (\"${defaultTopicConfig.description}\") must not be editable!")
    }

    private fun String.validateColor(): String {
        val exception =
            CatalystException("Invalid Color", "The color $this is invalid.", HttpStatus.UNPROCESSABLE_ENTITY)

        val trimmed = trim()

        var expectedLength = if (trimmed.startsWith("#")) 7 else 6

        if (trimmed.length != expectedLength)
            throw exception

        if (expectedLength == 7 && !(trimmed.substring(1).matches("[a-zA-Z0-9]+".toRegex())))
            throw exception

        return if (expectedLength == 7) trimmed.substring(1) else this
    }

    fun createTopic(name: String, description: String, color: String, editable: Boolean = true): TopicEntity {
        val entity = TopicEntity()
        entity.name = name
        entity.description = description
        entity.editable = editable
        entity.color = color.validateColor()
        entity.textColor = textColor(color)

        topicRepository.save(entity)

        return entity
    }

    fun findTopic(name: String, description: String): TopicEntity? =
        topicRepository.getByNameAndDescription(name, description)

    fun getAllTopics(): List<TopicEntity> = topicRepository.findAll()

    fun retrieveTopic(id: UUID): TopicEntity = topicRepository.findById(id).orElseThrow {
        CatalystException(
            "Unknown Topic",
            "Could not modify undefined topic $id",
            HttpStatus.NOT_FOUND
        )
    }

    fun updateTopic(id: UUID, name: String?, description: String?, color: String?) {
        val entity = retrieveTopic(id)

        if (color != null) {
            entity.color = color.validateColor()
            entity.textColor = textColor(color)
        }

        if (name != null)
            entity.name = name

        if (description != null)
            entity.description = description

        topicRepository.save(entity)
    }

    private fun removeTopicEntity(topic: TopicEntity) {
        topicRepository.delete(topic);
    }

    fun removeTopic(id: UUID, fallback: TopicEntity) {
        val topic = retrieveTopic(id)

        if (topic.id == fallback.id)
            throw CatalystException("Topic Removal Failed", "Cannot remove fallback topic.", HttpStatus.BAD_REQUEST)

        articleRepository.getArticleEntitiesByTopic(topic).forEach { article ->
            article.topic = fallback
            articleRepository.save(article)
        }

        removeTopicEntity(topic);
    }

}