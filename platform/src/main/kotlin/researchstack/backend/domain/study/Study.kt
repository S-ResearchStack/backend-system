package researchstack.backend.domain.study

import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.StudyParticipationApprovalType
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage
import java.time.LocalDateTime

data class Study(
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
        val targetSubject: Long? = null,
        val startDate: LocalDateTime? = null,
        val endDate: LocalDateTime? = null
    )

    data class IrbInfo(
        val irbDecisionType: IrbDecisionType,
        val decidedAt: LocalDateTime? = null,
        val expiredAt: LocalDateTime? = null
    )

    companion object {
        fun new(
            id: String,
            participationCode: String?,
            name: String,
            description: String?,
            participationApprovalType: StudyParticipationApprovalType,
            scope: StudyScope,
            stage: StudyStage,
            logoUrl: String?,
            imageUrl: String?,
            irbDecisionType: IrbDecisionType,
            decidedAt: LocalDateTime? = null,
            expiredAt: LocalDateTime? = null,
            organization: String,
            duration: String,
            period: String,
            requirements: List<String>,
            targetSubject: Long? = null,
            startDate: LocalDateTime? = null,
            endDate: LocalDateTime? = null
        ): Study = Study(
            id = id,
            participationCode = participationCode,
            studyInfo = StudyInfo(
                name = name,
                description = description,
                participationApprovalType = participationApprovalType,
                scope = scope,
                stage = stage,
                logoUrl = logoUrl,
                imageUrl = imageUrl,
                organization = organization,
                duration = duration,
                period = period,
                requirements = requirements,
                targetSubject = targetSubject,
                startDate = startDate,
                endDate = endDate
            ),
            irbInfo = IrbInfo(
                irbDecisionType = irbDecisionType,
                decidedAt = decidedAt,
                expiredAt = expiredAt
            )
        )
    }
}
