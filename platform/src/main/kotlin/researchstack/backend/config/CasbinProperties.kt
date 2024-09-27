package researchstack.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("casbin")
data class CasbinProperties(
    val model: String,
    val policy: String,
    val policyTopic: String
)
