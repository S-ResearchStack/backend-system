package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("totalCaloriesBurned")
data class TotalCaloriesBurnedEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    @JsonProperty("start_time")
    val startTime: Timestamp,
    @JsonProperty("end_time")
    val endTime: Timestamp,
    val calories: Double,
    @JsonProperty("time_offset")
    val timeOffset: String? = null
) : HealthDataEntity
