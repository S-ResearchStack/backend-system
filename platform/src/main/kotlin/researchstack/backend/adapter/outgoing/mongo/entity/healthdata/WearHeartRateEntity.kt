package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("wearHeartRate")
data class WearHeartRateEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    val value: Long,
    @JsonProperty("heartRateStatus")
    val status: Long,
    val ibiList: List<Long>,
    val ibiStatusList: List<Long>,
    val timestamp: Timestamp,
    @JsonProperty("time_offset")
    val timeOffset: String? = null
) : HealthDataEntity
