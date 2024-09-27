package researchstack.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("oidc.google")
data class OidcGoogleProperties(
    val oauth2Url: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String
)
