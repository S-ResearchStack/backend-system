package researchstack.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.invitation")
data class InvitationProperties(
    val url: String
)
