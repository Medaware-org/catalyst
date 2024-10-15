package org.medaware.catalyst.rest.tangential

import org.medaware.catalyst.api.TangentialAuthApi
import org.medaware.catalyst.dto.AccountUpdateRequest
import org.medaware.catalyst.dto.BasicMaintainerResponse
import org.medaware.catalyst.dto.TangentialLoginRequest
import org.medaware.catalyst.dto.TangentialSessionResponse
import org.medaware.catalyst.security.currentSession
import org.medaware.catalyst.service.MaintainerService
import org.medaware.catalyst.service.SessionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class TangentialAuthController(
    val maintainerService: MaintainerService,
    val sessionService: SessionService
) : TangentialAuthApi {

    override fun tangentialLogin(tangentialLoginRequest: TangentialLoginRequest): ResponseEntity<TangentialSessionResponse> {
        Thread.sleep(1000)
        return ResponseEntity.ok(maintainerService.login(tangentialLoginRequest).toDto())
    }

    override fun tangentialLogout(): ResponseEntity<Unit> {
        sessionService.invalidateCurrentSession()
        return ResponseEntity.ok().build()
    }

    override fun tangentialWhoAmI(): ResponseEntity<BasicMaintainerResponse> {
        return ResponseEntity.ok(maintainerService.currentMaintainer().toBasicDto())
    }

    override fun updateProfileDetails(accountUpdateRequest: AccountUpdateRequest): ResponseEntity<Unit> {
        maintainerService.updateAccountDetails(currentSession().maintainer, accountUpdateRequest)
        return ResponseEntity.ok().build()
    }

}