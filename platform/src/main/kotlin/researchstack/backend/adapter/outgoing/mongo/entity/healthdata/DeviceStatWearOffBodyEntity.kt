package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("deviceStatWearOffBody")
data class DeviceStatWearOffBodyEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    val isWearableOffBody: Long,
    val timeOffset: Long,
    val timestamp: Timestamp
) : HealthDataEntity
