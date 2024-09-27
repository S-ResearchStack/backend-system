package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("deviceStatMobileWearConnection")
data class DeviceStatMobileWearConnectionEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    val wearableDeviceName: String,
    val timeOffset: Long,
    val timestamp: Timestamp
) : HealthDataEntity
