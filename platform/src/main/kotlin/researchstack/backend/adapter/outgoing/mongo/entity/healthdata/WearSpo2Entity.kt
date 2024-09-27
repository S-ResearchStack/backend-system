package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("wearSpo2")
data class WearSpo2Entity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    val heartRate: Long,
    @JsonProperty("spO2")
    val spo2: Long,
    val status: Long,
    val timestamp: Timestamp,
    @JsonProperty("time_offset")
    val timeOffset: String? = null
) : HealthDataEntity
