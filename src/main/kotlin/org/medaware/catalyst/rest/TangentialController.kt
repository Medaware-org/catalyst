package org.medaware.catalyst.rest

import org.medaware.catalyst.api.TangentialApi
import org.medaware.catalyst.dto.ArticleResponse
import org.medaware.catalyst.dto.TangentialLoginRequest
import org.medaware.catalyst.dto.TangentialSessionResponse
import org.medaware.catalyst.security.currentSession
import org.medaware.catalyst.service.ArticleService
import org.medaware.catalyst.service.MaintainerService
import org.medaware.catalyst.service.SessionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

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

    override fun listOwnArticles(): ResponseEntity<List<ArticleResponse>> {
        return ResponseEntity.ok(articleService.getDtosOfArticlesWrittenBy(currentSession().maintainer))
    }
}