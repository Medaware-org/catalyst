package org.medaware.catalyst.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter

class CorsFilter : OncePerRequestFilter() {

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
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "*")
        response.setHeader("Access-Control-Allow-Headers", "*")
        response.setHeader("Access-Control-Allow-Credentials", "true")

        if (request.method != "OPTIONS") {
            filterChain.doFilter(request, response)
            return
        }

        response.status = HttpStatus.OK.value()
        return
    }

}