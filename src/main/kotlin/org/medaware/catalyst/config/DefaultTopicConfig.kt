package org.medaware.catalyst.config

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("catalyst.default-topic")
class DefaultTopicConfig {

    var name: String = ""
    var description: String = ""
    var color: String = ""

    @PostConstruct
    fun validate() {
        if (name.isEmpty() || description.isEmpty() || color.isEmpty())
            throw IllegalArgumentException("The name, description and color of the default topic must not be empty.")
    }

}