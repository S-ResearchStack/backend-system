package researchstack.backend.application.port.incoming.study

import researchstack.backend.application.port.incoming.task.TaskResultCommand.SurveyResult
import researchstack.backend.application.service.mapper.StudyMapper
import researchstack.backend.domain.study.EligibilityTestResult
import researchstack.backend.domain.study.SignedInformedConsent

data class ParticipateInStudyCommand(
    val eligibilityTestResultCommand: EligibilityTestResultCommand?,
    val signedInformedConsentCommand: SignedInformedConsentCommand
) {
    class EligibilityTestResultCommand(val result: SurveyResult) {
        fun toDomain(): EligibilityTestResult = StudyMapper.INSTANCE.toDomain(this)
    }

    class SignedInformedConsentCommand(val imagePath: String) {
        fun toDomain(): SignedInformedConsent = StudyMapper.INSTANCE.toDomain(this)
    }
}
