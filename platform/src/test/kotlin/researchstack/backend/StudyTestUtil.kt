package researchstack.backend

import researchstack.backend.application.port.incoming.study.CreateStudyCommand
import researchstack.backend.application.port.incoming.study.StudyResponse
import researchstack.backend.application.port.incoming.study.UpdateStudyCommand
import researchstack.backend.domain.study.Study
import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.StudyParticipationApprovalType
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage
import java.time.LocalDateTime

class StudyTestUtil {
    companion object {
        const val participationCode = "secret"
        const val studyName = "Dummy Study"
        const val studyDescription = "description for dummy study"
        const val logoUrl = "https://dummy.com/logo.png"
        const val imageUrl = "https://dummy.com/image.png"
        const val organization = "Samsung Medical Center"
        const val duration = "Takes about 15 minutes a week"
        const val period = "Takes about 1 month in total"
        val requirements = listOf(
            "Complete daily check-in",
            "You have to wear Galaxy Watch all day except when you're asleep"
        )

        val participationApprovalType = StudyParticipationApprovalType.AUTO
        val studyScope = StudyScope.PRIVATE
        val studyStage = StudyStage.STARTED_OPEN

        val studyInfoResponse = StudyResponse.StudyInfoResponse(
            studyName,
            studyDescription,
            participationApprovalType,
            studyScope,
            studyStage,
            logoUrl,
            imageUrl,
            organization,
            duration,
            period,
            requirements
        )
        val now = LocalDateTime.now()
        val decisionType = IrbDecisionType.APPROVED
        val decidedAt = now
        val expiredAt = now.plusMonths(6)
        val irbInfoResponse = StudyResponse.IrbInfoResponse(
            decisionType,
            decidedAt,
            expiredAt
        )
        val studyId = "mentalCareStudy"
        val studyResponse = StudyResponse(
            studyId,
            participationCode,
            studyInfoResponse,
            irbInfoResponse
        )

        private const val targetSubject = 150L
        private val startDate = LocalDateTime.now()
        private val endDate = startDate.plusMonths(6)

        fun createCreateStudyCommand(studyId: String?) = CreateStudyCommand(
            id = studyId ?: "mentalCareStudy",
            participationCode = participationCode,
            name = studyName,
            description = studyDescription,
            participationApprovalType = StudyParticipationApprovalType.AUTO,
            scope = StudyScope.PRIVATE,
            stage = StudyStage.STARTED_OPEN,
            logoUrl = logoUrl,
            imageUrl = imageUrl,
            organization = organization,
            duration = duration,
            period = period,
            requirements = requirements,
            irbDecisionType = IrbDecisionType.APPROVED,
            irbDecidedAt = decidedAt,
            irbExpiredAt = expiredAt,
            targetSubject = targetSubject,
            startDate = startDate,
            endDate = endDate
        )

        fun createUpdateStudyCommand() = UpdateStudyCommand(
            participationCode = participationCode,
            name = studyName,
            description = studyDescription,
            participationApprovalType = StudyParticipationApprovalType.AUTO,
            scope = StudyScope.PRIVATE,
            stage = StudyStage.STARTED_OPEN,
            logoUrl = logoUrl,
            imageUrl = imageUrl,
            organization = organization,
            duration = duration,
            period = period,
            requirements = requirements,
            irbDecisionType = IrbDecisionType.APPROVED,
            irbDecidedAt = decidedAt,
            irbExpiredAt = expiredAt,
            targetSubject = targetSubject,
            startDate = startDate,
            endDate = endDate
        )

        fun createDummyStudy(studyId: String): Study {
            val command = createCreateStudyCommand(studyId)
            return command.toDomain()
        }
    }
}
