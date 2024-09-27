package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("wearAccelerometer")
data class WearAccelerometerEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    val x: Long,
    val y: Long,
    val z: Long,
    val timestamp: Timestamp,
    @JsonProperty("time_offset")
    val timeOffset: String? = null
) : HealthDataEntity
