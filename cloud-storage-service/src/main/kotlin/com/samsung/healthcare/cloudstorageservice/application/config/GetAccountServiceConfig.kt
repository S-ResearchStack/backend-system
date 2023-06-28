package com.samsung.healthcare.cloudstorageservice.application.config

import com.samsung.healthcare.account.application.service.GetAccountService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder

@Configuration
class GetAccountServiceConfig {

    @Bean
    fun getAccountService(properties: JwkProperties) =
        GetAccountService(NimbusReactiveJwtDecoder(properties.url))
}
