package org.medaware.catalyst.config.catalyst

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "catalyst.minio")
class CatalystMinIoConfig {

    var endpoint: String = ""
    var accessKey: String = ""
    var secretKey: String = ""
    var bucket: String = ""

    @PostConstruct
    fun validate() {
        if (endpoint.isEmpty() || accessKey.isEmpty() || secretKey.isEmpty() || bucket.isEmpty())
            throw IllegalArgumentException("The MinIO endpoint, access key, secret key and bucket must not be empty (catalyst.minio.endpoint, catalyst.minio.accessKey, catalyst.minio.secretKey, catalyst.minio.bucket)")
    }

}