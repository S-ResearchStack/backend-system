package com.samsung.healthcare.account.adapter.auth.config

import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactivefeign.webclient.WebReactiveFeign

@Configuration
class SuperTokenConfig {

    @Value("\${superTokens.url}")
    private var url = "http://localhost:3567"

    @Bean
    fun superTokensApi() =
        WebReactiveFeign.builder<SuperTokensApi>()
            .target(SuperTokensApi::class.java, url)
}
