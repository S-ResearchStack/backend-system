package researchstack.backend.application.port.incoming.study

import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.service.mapper.StudyMapper
import researchstack.backend.domain.study.Study
import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.StudyParticipationApprovalType
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage
import java.time.LocalDateTime

data class CreateStudyCommand(
    val id: String,
    val participationCode: String?,
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
    val irbDecisionType: IrbDecisionType,
    val irbDecidedAt: LocalDateTime? = null,
    val irbExpiredAt: LocalDateTime? = null,
    val targetSubject: Long? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null
) {
    init {
        require(id.isNotBlank()) { ExceptionMessage.EMPTY_STUDY_ID }
        require(name.isNotBlank()) { ExceptionMessage.EMPTY_STUDY_NAME }
    }

    fun toDomain(): Study = StudyMapper.INSTANCE.toDomain(this)
}
