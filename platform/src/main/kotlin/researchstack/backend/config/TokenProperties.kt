package researchstack.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("token")
data class TokenProperties(
    val accessToken: AccessToken,
    val refreshToken: RefreshToken
) {
    data class AccessToken(
        val lifetime: Long,
        val issuer: String
    )

    data class RefreshToken(
        val lifetime: Long,
        val length: Int
    )
}
