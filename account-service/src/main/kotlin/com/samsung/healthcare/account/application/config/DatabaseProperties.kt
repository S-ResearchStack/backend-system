package com.samsung.healthcare.account.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.db")
data class DatabaseProperties(
    val url: String,
    val user: String,
    val password: String,
)
