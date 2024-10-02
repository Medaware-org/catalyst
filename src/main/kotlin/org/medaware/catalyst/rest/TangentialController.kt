package org.medaware.catalyst.rest

import org.medaware.catalyst.api.TangentialApi
import org.medaware.catalyst.dto.TangentialLoginRequest
import org.medaware.catalyst.dto.TangentialSessionResponse
import org.medaware.catalyst.service.MaintainerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class TangentialController(
    val maintainerService: MaintainerService
) : TangentialApi {

    override fun tangentialLogin(tangentialLoginRequest: TangentialLoginRequest): ResponseEntity<TangentialSessionResponse> {
        Thread.sleep(1000)
        return ResponseEntity.ok(maintainerService.login(tangentialLoginRequest).toDto())
    }

}