package org.medaware.catalyst.config.catalyst

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "catalyst.ocr")
class CatalystOcrConfiguration {
    var url: String = "http://localhost:5000"

    @PostConstruct
    fun postConstruct() {
        LoggerFactory.getLogger(CatalystOcrConfiguration::class.java).info("OCR Server URL: $url")
    }
}