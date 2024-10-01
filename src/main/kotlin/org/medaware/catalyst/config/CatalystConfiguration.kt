package org.medaware.catalyst.config

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "catalyst.default-access")
class CatalystConfiguration {
    var username: String = ""
    var password: String = ""

    @PostConstruct
    fun validate() {
        if (username.isEmpty() || password.isEmpty())
            throw IllegalArgumentException("The properties catalyst.default-access.username and catalyst.default-access.password must be set")
    }

}