package com.samsung.healthcare.cloudstorageservice.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.jwk")
data class JwkProperties(
    val url: String
)
