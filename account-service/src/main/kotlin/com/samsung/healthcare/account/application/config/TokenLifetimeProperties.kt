package com.samsung.healthcare.account.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.token-lifetime")
data class TokenLifetimeProperties(
    val accessToken: Long,
    val refreshToken: Long,
)
