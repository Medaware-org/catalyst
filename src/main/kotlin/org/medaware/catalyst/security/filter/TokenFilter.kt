package org.medaware.catalyst.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.medaware.catalyst.persistence.service.TokenService
import org.medaware.catalyst.security.authentication.CatalystAuthentication
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TokenFilter(
    var tokenService: TokenService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorization = request.getHeader("Authorization") ?: run {
            sendUnauthorized(response)
            return
        }

        val user = tokenService.userOfToken(authorization) ?: run {
            sendUnauthorized(response)
            return
        }

        SecurityContextHolder.getContext().authentication = CatalystAuthentication(user, authorization)

        filterChain.doFilter(request, response)
    }

    fun sendUnauthorized(response: HttpServletResponse) {
        response.contentType = "text/plain"
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.print("Unauthorized")
    }

}