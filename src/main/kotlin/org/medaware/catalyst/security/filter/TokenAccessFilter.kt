package org.medaware.catalyst.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.medaware.catalyst.persistence.model.SessionEntity
import org.medaware.catalyst.security.authentication.CatalystAuthentication
import org.medaware.catalyst.service.SessionService
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TokenAccessFilter(
    val sessionService: SessionService
) : OncePerRequestFilter() {

    fun HttpServletResponse.sendUnauthorized() {
        this.contentType = "text/plain"
        this.writer.write("Unauthorized")
        this.status = HttpStatus.UNAUTHORIZED.value()
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val token: String? = request.getHeader("Authorization")

        if (token == null) {
            response.sendUnauthorized()
            return
        }

        val session: SessionEntity =
            sessionService.getValidSession(token) ?: run {
                response.sendUnauthorized()
                return
            }

        SecurityContextHolder.getContext().authentication = CatalystAuthentication(session)

        filterChain.doFilter(request, response)
    }

}