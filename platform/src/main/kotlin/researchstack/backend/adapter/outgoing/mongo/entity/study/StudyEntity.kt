package researchstack.backend.adapter.outgoing.mongo.entity.study

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.StudyParticipationApprovalType
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage
import java.time.LocalDateTime

@Document("study")
data class StudyEntity(
    @Id
    val id: String,
    val participationCode: String?,
    val studyInfo: StudyInfo,
    val irbInfo: IrbInfo
) {
    data class StudyInfo(
        val name: String,
        val description: String?,
        val participationApprovalType: StudyParticipationApprovalType,
        val scope: StudyScope,
        val stage: StudyStage,
        val logoUrl: String?,
        val imageUrl: String?,
        val organization: String,
        val duration: String,
        val period: String,
        val requirements: List<String>,
        val targetSubject: Int? = null,
        val startDate: LocalDateTime? = null,
        val endDate: LocalDateTime? = null
    )

    data class IrbInfo(
        val irbDecisionType: IrbDecisionType,
        val decidedAt: LocalDateTime? = null,
        val expiredAt: LocalDateTime? = null
    )
}
