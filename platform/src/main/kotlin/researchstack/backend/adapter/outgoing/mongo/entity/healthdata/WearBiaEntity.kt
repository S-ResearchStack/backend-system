package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("wearBia")
data class WearBiaEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    val basalMetabolicRate: Double,
    val bodyFatMass: Double,
    val bodyFatRatio: Double,
    val fatFreeMass: Double,
    val fatFreeRatio: Double,
    val skeletalMuscleMass: Double,
    val skeletalMuscleRatio: Double,
    val totalBodyWater: Double,
    val measurementProgress: Double,
    val status: Long,
    val timestamp: Timestamp,
    @JsonProperty("time_offset")
    val timeOffset: String? = null
) : HealthDataEntity
