package com.samsung.healthcare.account.application.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder

@Configuration
class ReactiveJwtDecoderConfig {

    @Bean
    fun reactiveJwtDecoder(jwkProperties: JwkProperties): ReactiveJwtDecoder =
        NimbusReactiveJwtDecoder(jwkProperties.url)
}
