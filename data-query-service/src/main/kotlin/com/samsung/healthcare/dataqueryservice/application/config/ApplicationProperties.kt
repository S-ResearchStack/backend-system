package com.samsung.healthcare.dataqueryservice.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("config")
data class ApplicationProperties(
    val db: Db,
    val trino: Trino,
    val jwks: JwksConfig,
) {
    data class Db(
        val prefix: String,
        val postfix: String,
    )

    data class Trino(
        val url: String,
        val catalog: String,
        val user: String,
    )

    data class JwksConfig(
        val url: String
    )
}
