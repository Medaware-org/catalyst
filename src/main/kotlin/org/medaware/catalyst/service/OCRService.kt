package org.medaware.catalyst.service

import org.medaware.catalyst.config.catalyst.CatalystOcrConfiguration
import org.medaware.catalyst.exception.CatalystException
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

data class OCRResponse(val data: List<String>)

data class GHSResponse(val output: List<String>, val scores: Map<String, Double>)

@Service
class OCRService(
    val ocrConfiguration: CatalystOcrConfiguration
) {

    private val httpClient = WebClient.create(ocrConfiguration.url)

    private fun ByteArray.asResource(filename: String) = object : ByteArrayResource(this) {
        override fun getFilename() = filename
    }

    fun requestOcr(filename: String, imageBytes: ByteArray): OCRResponse = try {
        httpClient.post()
            .uri("/api/ocr")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData("data", imageBytes.asResource(filename)))
            .retrieve()
            .bodyToMono(OCRResponse::class.java)
            .block() ?: throw CatalystException(
            "OCR Request Failed",
            "The request to the OCR server failed",
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    } catch (e: Exception) {
        e.printStackTrace()
        throw CatalystException("Connection Failed", "Could not connect to the OCR server", HttpStatus.NOT_FOUND)
    }

    fun requestGhs(filename: String, imageBytes: ByteArray): GHSResponse = try {
        httpClient.post()
            .uri("/api/cnn")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData("data", imageBytes.asResource(filename)))
            .retrieve()
            .bodyToMono(GHSResponse::class.java)
            .block() ?: throw CatalystException(
            "OCR Request Failed",
            "The request to the OCR server failed",
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    } catch (e: Exception) {
        e.printStackTrace()
        throw CatalystException("Connection Failed", "Could not connect to the OCR server", HttpStatus.NOT_FOUND)
    }

}