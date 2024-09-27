package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("wearBloodPressure")
data class WearBloodPressureEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    val sbp: Int,
    val dbp: Int,
    val timestamp: Timestamp,
    @JsonProperty("time_offset")
    val timeOffset: String? = null
) : HealthDataEntity
