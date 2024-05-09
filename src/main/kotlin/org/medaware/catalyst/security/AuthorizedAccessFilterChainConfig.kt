package org.medaware.catalyst.security

import org.medaware.catalyst.persistence.service.TokenService
import org.medaware.catalyst.security.filter.TokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class AuthorizedAccessFilterChainConfig(
    val tokenService: TokenService
) {

    @Bean
    @Order(LOWEST_PRECEDENCE)
    fun authorizedFilterChainConfig(http: HttpSecurity): SecurityFilterChain {

        http {
            cors { disable() }
            csrf { disable() }

            securityMatcher("/**")

            authorizeRequests {
                authorize(anyRequest, authenticated)
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(TokenFilter(tokenService))

        }

        return http.build()
    }

}