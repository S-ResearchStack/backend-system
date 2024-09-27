package researchstack.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwk.url")
data class JwkProperties(
    val samsungAccount: String,
    val google: String,
    val cognito: String,
    val superTokens: String
)
