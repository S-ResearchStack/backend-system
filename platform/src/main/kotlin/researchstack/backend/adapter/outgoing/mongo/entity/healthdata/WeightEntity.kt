package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("weight")
data class WeightEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    @JsonProperty("start_time")
    val startTime: Timestamp,
    @JsonProperty("time_offset")
    val timeOffset: String? = null,
    val weight: Double,
    val height: Double? = null,
    @JsonProperty("body_fat")
    val bodyFat: Double? = null,
    @JsonProperty("skeletal_muscle")
    val skeletalMuscle: Double? = null,
    @JsonProperty("muscle_mass")
    val muscleMass: Double? = null,
    @JsonProperty("basal_metabolic_rate")
    val basalMetabolicRate: Long? = null,
    @JsonProperty("body_fat_mass")
    val bodyFatMass: Double? = null,
    @JsonProperty("fat_free_mass")
    val fatFreeMass: Double? = null,
    @JsonProperty("fat_free")
    val fatFree: Double? = null,
    @JsonProperty("skeletal_muscle_mass")
    val skeletalMuscleMass: Double? = null,
    @JsonProperty("total_body_water")
    val totalBodyWater: Double? = null
) : HealthDataEntity
