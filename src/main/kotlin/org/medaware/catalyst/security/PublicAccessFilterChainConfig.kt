package org.medaware.catalyst.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class PublicAccessFilterChainConfig {

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    fun publicFilterChainConfig(http: HttpSecurity): SecurityFilterChain {

        http {
            cors { disable() }
            csrf { disable() }

            securityMatcher(
                "/",
                "/login",
                "/register",
                "/search",
                "/swagger-ui/**",
                "/v3/**",
                "/spec.yaml",
                "/swagger-ui/index.html",
                "/public/**"
            )

            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
        }

        return http.build()
    }

}