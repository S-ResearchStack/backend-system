package researchstack.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.data.mongodb")
class MongoDbProperties(
    val database: String
)
