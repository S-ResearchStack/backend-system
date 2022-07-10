package com.samsung.healthcare.dataqueryservice.application.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

@Configuration
class JwtDecoderConfig {
    @Bean
    fun jwtDecoder(config: ApplicationProperties): JwtDecoder {
        return NimbusJwtDecoder.withJwkSetUri(config.jwks.url).build()
    }
}
