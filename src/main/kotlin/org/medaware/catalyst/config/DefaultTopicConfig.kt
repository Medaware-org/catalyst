package org.medaware.catalyst.config

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("catalyst.default-topic")
class DefaultTopicConfig {

    var name: String = ""
    var description: String = ""

    @PostConstruct
    fun validate() {
        if (name.isEmpty() || description.isEmpty())
            throw IllegalArgumentException("The name and description of the default topic must not be empty.")
    }

}