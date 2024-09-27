package researchstack.backend.adapter.outgoing.mongo.entity.inlabvisit

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("inLabVisit")
data class InLabVisitEntity(
    @Id
    val id: String? = null,
    val picId: String,
    val subjectNumber: String,
    val note: String? = null,
    val filePaths: List<String>? = null,
    val creatorId: String,
    val modifierId: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val modifiedAt: LocalDateTime? = null
)
