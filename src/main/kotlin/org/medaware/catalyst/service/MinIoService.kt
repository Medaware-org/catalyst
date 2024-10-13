package org.medaware.catalyst.service

import io.minio.BucketExistsArgs
import io.minio.GetObjectArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.errors.ErrorResponseException
import jakarta.annotation.PostConstruct
import org.medaware.catalyst.config.catalyst.CatalystMinIoConfig
import org.medaware.catalyst.exception.CatalystException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.util.Base64

@Service
class MinIoService(
    val catalystMinIoConfig: CatalystMinIoConfig
) {

    val logger = LoggerFactory.getLogger(javaClass)

    lateinit var minioClient: MinioClient

    @PostConstruct
    fun connect() {
        minioClient = MinioClient
            .builder()
            .endpoint(catalystMinIoConfig.endpoint)
            .credentials(catalystMinIoConfig.accessKey, catalystMinIoConfig.secretKey)
            .build()

        val bucket = catalystMinIoConfig.bucket

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
            logger.info("Created default bucket '$bucket'")
        }
    }

    fun upload(objectName: String, data: ByteArray) {
        minioClient.putObject(
            PutObjectArgs
                .builder()
                .bucket(catalystMinIoConfig.bucket)
                .`object`(objectName)
                .stream(data.inputStream(), data.size.toLong(), -1)
                .build()
        )
    }

    fun retrieve(objectName: String, failIfNotExists: Boolean = true): ByteArray? {
        val response = try {
            minioClient.getObject(
                GetObjectArgs
                    .builder()
                    .bucket(catalystMinIoConfig.bucket)
                    .`object`(objectName)
                    .build()
            )
        } catch (e: ErrorResponseException) {
            if (failIfNotExists)
                throw CatalystException(
                    "Resource not found", "Could not find the requested resource ID \"$objectName\"",
                    HttpStatus.NOT_FOUND
                )
            else
                return null
        }
        val bytes = response.readAllBytes()
        response.close()
        return bytes
    }

    fun storeOrRetrieveResource(url: String): String {
        val objectName = Base64.getEncoder().encodeToString(url.toByteArray())
        var retrievedObject = retrieve(objectName, failIfNotExists = false)
        if (retrievedObject != null)
            return objectName
        val template = RestTemplate()
        val data = try {
            template.getForObject(url, ByteArray::class.java)
        } catch (e: RestClientException) {
            throw CatalystException(
                "Resource not found",
                "Could not find the requested resource at \"$url\"",
                HttpStatus.NOT_FOUND
            )
        }
        upload(objectName, data!!)
        logger.info("Resource stored - \"$url\"")
        return objectName
    }

}