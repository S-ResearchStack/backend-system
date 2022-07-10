package com.samsung.healthcare.platform.application.config

import com.samsung.healthcare.account.application.service.GetAccountService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder

@Configuration
class GetAccountServiceConfig {

    @Bean
    fun getAccountService(appProps: ApplicationProperties) =
        GetAccountService(NimbusReactiveJwtDecoder(appProps.jwks.url))
}
