package com.samsung.healthcare.account.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.verification")
data class EmailVerificationProperties(
    val url: String,
)
