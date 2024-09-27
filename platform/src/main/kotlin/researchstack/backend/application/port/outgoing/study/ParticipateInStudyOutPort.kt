package researchstack.backend.application.port.outgoing.study

import researchstack.backend.domain.study.EligibilityTestResult
import researchstack.backend.domain.study.SignedInformedConsent
import researchstack.backend.enums.SubjectStatus

interface ParticipateInStudyOutPort {
    suspend fun participateInStudy(
        subjectId: String,
        studyId: String,
        subjectNumber: String,
        subjectStatus: SubjectStatus,
        sessionId: String,
        eligibilityTestResult: EligibilityTestResult?,
        signedInformedConsent: SignedInformedConsent
    )
}
