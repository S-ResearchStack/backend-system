package researchstack.backend.application.port.incoming.study

import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.StudyParticipationApprovalType
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage
import java.time.LocalDateTime

data class StudyResponse(
    val id: String,
    val participationCode: String?,
    val studyInfoResponse: StudyInfoResponse,
    val irbInfoResponse: IrbInfoResponse
) {

    data class StudyInfoResponse(
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
        val targetSubject: Long? = null,
        val startDate: LocalDateTime? = null,
        val endDate: LocalDateTime? = null
    )

    data class IrbInfoResponse(
        val decisionType: IrbDecisionType,
        val decidedAt: LocalDateTime? = null,
        val expiredAt: LocalDateTime? = null
    )
}
