package org.medaware.catalyst.rest

import org.medaware.catalyst.api.TopicsApi
import org.medaware.catalyst.dto.DeleteTopicRequest
import org.medaware.catalyst.dto.TopicCreationRequest
import org.medaware.catalyst.dto.TopicResponse
import org.medaware.catalyst.dto.UpdateTopicRequest
import org.medaware.catalyst.service.TopicService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicsController(
    val topicService: TopicService
) : TopicsApi {

    override fun createNewTopic(topicCreationRequest: TopicCreationRequest): ResponseEntity<Unit> {
        topicService.createTopic(
            topicCreationRequest.name,
            topicCreationRequest.description,
            topicCreationRequest.color
        )
        return ResponseEntity.ok().build()
    }

    override fun deleteTopic(deleteTopicRequest: DeleteTopicRequest): ResponseEntity<Unit> {
        topicService.removeTopic(deleteTopicRequest.id, topicService.getFallbackTopic())
        return ResponseEntity.ok().build()
    }

    override fun getAllTopics(): ResponseEntity<List<TopicResponse>> {
        return ResponseEntity.ok(topicService.getAllTopics().map {
            it.toDto()
        })
    }

    override fun updateTopic(updateTopicRequest: UpdateTopicRequest): ResponseEntity<Unit> {
        return ResponseEntity.ok(
            topicService.updateTopic(
                updateTopicRequest.id,
                updateTopicRequest.name,
                updateTopicRequest.description,
                updateTopicRequest.color
            )
        )
    }
}