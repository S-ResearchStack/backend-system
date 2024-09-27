package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("exercise")
data class ExerciseEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    @JsonProperty("start_time")
    val startTime: Timestamp,
    @JsonProperty("time_offset")
    val timeOffset: String? = null,
    @JsonProperty("end_time")
    val endTime: Timestamp,
    @JsonProperty("exercise_type")
    val exerciseType: Long,
    @JsonProperty("exercise_custom_type")
    val exerciseCustomType: String? = null,
    val calorie: Double,
    val duration: Long,
    val distance: Double? = null,
    @JsonProperty("altitude_gain")
    val altitudeGain: Double? = null,
    @JsonProperty("altitude_loss")
    val altitudeLoss: Double? = null,
    val count: Long? = null,
    @JsonProperty("count_type")
    val countType: Long? = null,
    @JsonProperty("max_speed")
    val maxSpeed: Double? = null,
    @JsonProperty("mean_speed")
    val meanSpeed: Double? = null,
    @JsonProperty("max_caloriburn_rate")
    val maxCaloriburnRate: Double? = null,
    @JsonProperty("mean_caloriburn_rate")
    val meanCaloriburnRate: Double? = null,
    @JsonProperty("max_cadence")
    val maxCadence: Double? = null,
    @JsonProperty("mean_cadence")
    val meanCadence: Double? = null,
    @JsonProperty("max_heart_rate")
    val maxHeartRate: Double? = null,
    @JsonProperty("mean_heart_rate")
    val meanHeartRate: Double? = null,
    @JsonProperty("min_heart_rate")
    val minHeartRate: Double? = null,
    @JsonProperty("max_altitude")
    val maxAltitude: Double? = null,
    @JsonProperty("min_altitude")
    val minAltitude: Double? = null,
    @JsonProperty("incline_distance")
    val inclineDistance: Double? = null,
    @JsonProperty("decline_distance")
    val declineDistance: Double? = null,
    @JsonProperty("max_power")
    val maxPower: Double? = null,
    @JsonProperty("mean_power")
    val meanPower: Double? = null,
    @JsonProperty("mean_rpm")
    val meanRpm: Double? = null,
    @JsonProperty("max_rpm")
    val maxRpm: Double? = null,
    @JsonProperty("vo2_max")
    val vo2Max: Double? = null
) : HealthDataEntity
