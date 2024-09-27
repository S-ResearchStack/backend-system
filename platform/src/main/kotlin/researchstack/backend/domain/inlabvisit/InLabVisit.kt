package researchstack.backend.domain.inlabvisit

import kotlinx.serialization.Serializable
import researchstack.backend.adapter.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class InLabVisit(
    val id: String? = null,
    val picId: String,
    val subjectNumber: String,
    val note: String? = null,
    val filePaths: List<String>? = null,
    val creatorId: String,
    val modifierId: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val modifiedAt: LocalDateTime? = null
) {

    fun new(
        id: String? = null,
        picId: String? = null,
        subjectNumber: String? = null,
        note: String? = null,
        filePaths: List<String>? = null,
        creatorId: String? = null,
        modifierId: String? = null,
        startTime: LocalDateTime? = null,
        endTime: LocalDateTime? = null,
        createdAt: LocalDateTime? = null,
        modifiedAt: LocalDateTime? = null
    ): InLabVisit = InLabVisit(
        id = id ?: this.id,
        picId = picId ?: this.picId,
        subjectNumber = subjectNumber ?: this.subjectNumber,
        note = note ?: this.note,
        filePaths = filePaths ?: this.filePaths,
        creatorId = creatorId ?: this.creatorId,
        modifierId = modifierId ?: this.modifierId,
        startTime = startTime ?: this.startTime,
        endTime = endTime ?: this.endTime,
        createdAt = createdAt ?: this.createdAt,
        modifiedAt = modifiedAt ?: this.modifiedAt
    )
}
