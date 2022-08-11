package com.samsung.healthcare.platform.adapter.web.security

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val oAuth2SuccessHandler: OAuth2SuccessHandler,
) {
    @Bean
    fun firebaseApp(): FirebaseApp {
        // checks $GOOGLE_APPLICATION_CREDENTIALS
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build()

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.authorizeExchange {
            it.pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/api/**").permitAll()
                .anyExchange().permitAll()
        }
            .cors { it.disable() }
            .csrf { it.disable() }
            .httpBasic()
            .and()
            .oauth2Login {
                it.authenticationSuccessHandler(oAuth2SuccessHandler)
            }

        return http.build()
    }
}
