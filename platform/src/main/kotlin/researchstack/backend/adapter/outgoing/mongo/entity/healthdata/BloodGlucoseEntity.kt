package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("bloodGlucose")
data class BloodGlucoseEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    @JsonProperty("start_time")
    val startTime: Timestamp,
    @JsonProperty("time_offset")
    val timeOffset: String? = null,
    val glucose: Double,
    @JsonProperty("measurement_type")
    val measurementType: Long,
    @JsonProperty("meal_time")
    val mealTime: Long? = null,
    @JsonProperty("meal_type")
    val mealType: Long? = null,
    @JsonProperty("sample_source_type")
    val sampleSourceType: Long? = null
) : HealthDataEntity
