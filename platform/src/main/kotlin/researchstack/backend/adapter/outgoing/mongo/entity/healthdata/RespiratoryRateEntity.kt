package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("respiratoryRate")
data class RespiratoryRateEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String
    // TODO: define fields
) : HealthDataEntity
