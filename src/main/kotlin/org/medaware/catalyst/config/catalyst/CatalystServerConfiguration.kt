package org.medaware.catalyst.config.catalyst

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "catalyst")
class CatalystServerConfiguration {
    var server: String = ""

    @PostConstruct
    fun validate() {
        if (server.isEmpty())
            throw IllegalArgumentException("The server URL property (catalyst.server) must not be empty.")
    }

}