package researchstack.backend.adapter.outgoing.mongo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("versionInfo")
data class VersionEntity(
    @Id
    val id: String,
    val minimum: String,
    val latest: String
)
