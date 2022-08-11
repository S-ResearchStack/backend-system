package com.samsung.healthcare.platform.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("config")
data class ApplicationProperties(
    val db: Db,
    val newDatabaseConfig: NewDatabaseConfig,
) {
    data class Db(
        val url: String,
        val host: String,
        val port: Int,
        val name: String,
        val schema: String,
        val user: String,
        val password: String,
    )

    data class NewDatabaseConfig(
        val prefix: String,
        val postfix: String = ""
    )
}
