package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("wearEcg")
data class WearEcgEntity(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    @JsonProperty("ECG1_mv")
    val ecg1Mv: Double? = null,
    @JsonProperty("ECG2_mv")
    val ecg2Mv: Double? = null,
    @JsonProperty("ECG3_mv")
    val ecg3Mv: Double? = null,
    @JsonProperty("ECG4_mv")
    val ecg4Mv: Double? = null,
    @JsonProperty("ECG5_mv")
    val ecg5Mv: Double? = null,
    @JsonProperty("ECG6_mv")
    val ecg6Mv: Double? = null,
    @JsonProperty("ECG7_mv")
    val ecg7Mv: Double? = null,
    @JsonProperty("ECG8_mv")
    val ecg8Mv: Double? = null,
    @JsonProperty("ECG9_mv")
    val ecg9Mv: Double? = null,
    @JsonProperty("ECG10_mv")
    val ecg10Mv: Double? = null,
    @JsonProperty("PPG_Green")
    val ppgGreen: Long? = null,
    @JsonProperty("PPG_Green_Current")
    val ppgGreenCurrent: Long? = null,
    @JsonProperty("PPG_Green_iOffset")
    val ppgGreenIOffset: Long? = null,
    @JsonProperty("PPG_Green_TIAGain")
    val ppgGreenTiaGain: Long? = null,
    @JsonProperty("LeadOff")
    val leafOff: Long? = null,
    @JsonProperty("PosThreshold_mv")
    val posThresholdMv: Double? = null,
    @JsonProperty("NegThreshold_mv")
    val negThresholdMv: Double? = null,
    @JsonProperty("PktSeq#")
    val pktSeqNum: Long? = null,
    @JsonProperty("SessionId")
    val sessionId: Long? = null,
    val timestamp: Timestamp,
    @JsonProperty("time_offset")
    val timeOffset: String? = null
) : HealthDataEntity
