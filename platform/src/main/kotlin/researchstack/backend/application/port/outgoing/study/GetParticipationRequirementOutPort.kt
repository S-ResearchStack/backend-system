package researchstack.backend.application.port.outgoing.study

import researchstack.backend.domain.study.EligibilityTest
import researchstack.backend.domain.study.InformedConsent
import researchstack.backend.domain.study.ParticipationRequirement
import researchstack.backend.enums.HealthDataType

interface GetParticipationRequirementOutPort {
    suspend fun getParticipationRequirement(studyId: String): ParticipationRequirement
    suspend fun getEligibilityTest(studyId: String): EligibilityTest?
    suspend fun getInformedConsent(studyId: String): InformedConsent
    suspend fun getRequiredHealthDataTypeList(studyId: String): List<HealthDataType>
}
