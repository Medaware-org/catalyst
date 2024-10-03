package org.medaware.catalyst.rest

import org.medaware.catalyst.api.TangentialApi
import org.medaware.catalyst.dto.ArticleResponse
import org.medaware.catalyst.dto.TangentialLoginRequest
import org.medaware.catalyst.dto.TangentialSessionResponse
import org.medaware.catalyst.exception.CatalystException
import org.medaware.catalyst.security.currentSession
import org.medaware.catalyst.service.ArticleService
import org.medaware.catalyst.service.MaintainerService
import org.medaware.catalyst.service.SessionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class TangentialController(
    val maintainerService: MaintainerService,
    val sessionService: SessionService,
    val articleService: ArticleService
) : TangentialApi {

    override fun tangentialLogin(tangentialLoginRequest: TangentialLoginRequest): ResponseEntity<TangentialSessionResponse> {
        Thread.sleep(1000)
        return ResponseEntity.ok(maintainerService.login(tangentialLoginRequest).toDto())
    }

    override fun tangentialLogout(): ResponseEntity<Unit> {
        sessionService.invalidateCurrentSession()
        return ResponseEntity.ok().build()
    }

    override fun listArticles(selector: String): ResponseEntity<List<ArticleResponse>> {
        val uuid = try {
            UUID.fromString(selector)
        } catch (e: IllegalArgumentException) {
            null
        }

        if (uuid != null)
            return ResponseEntity.ok(articleService.getDtosOfArticlesBy(uuid))

        return ResponseEntity.ok(
            when (selector.lowercase()) {
                "current" -> articleService.getDtosOfArticlesBy(currentSession().maintainer)
                "all" -> articleService.getDtosOfAllArticles()
                else -> throw CatalystException(
                    "Unknown Selector",
                    "The article listing selector '${selector}' is undefined.",
                    HttpStatus.UNPROCESSABLE_ENTITY
                )
            }
        )
    }
}