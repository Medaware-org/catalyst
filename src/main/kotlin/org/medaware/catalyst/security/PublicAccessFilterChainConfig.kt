package org.medaware.catalyst.security

import okhttp3.internal.http.HttpMethod
import org.medaware.catalyst.security.filter.CorsFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.util.AntPathMatcher

@Configuration
class PublicAccessFilterChainConfig {

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    fun publicAccessFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            securityMatcher("/", "/swagger-ui/**", "/catalyst-api-docs", "/v3/**", "/tan/login", "/avis/css", "/rsrc/**", "/query/**", "/tan/articles/all", "/articles", "/ocr", "/ghs")
            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(CorsFilter())
        }
        return http.build()
    }

}