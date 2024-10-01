package org.medaware.catalyst.security

import org.medaware.catalyst.security.filter.TokenAccessFilter
import org.medaware.catalyst.service.SessionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class TokenAccessFilterChainConfig(
    val sessionService: SessionService
) {

    @Bean
    @Order(LOWEST_PRECEDENCE)
    fun tokenAccessFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            cors { disable() }
            securityMatcher("**")
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(TokenAccessFilter(sessionService))
        }
        return http.build()
    }

}