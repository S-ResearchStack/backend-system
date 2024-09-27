package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import org.springframework.data.mongodb.core.mapping.Document

@Document("healthDataGroup")
data class HealthDataGroupEntity(
    val name: String,
    val types: List<String>
)
