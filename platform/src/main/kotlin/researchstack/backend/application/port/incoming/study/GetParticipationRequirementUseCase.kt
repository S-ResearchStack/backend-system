package researchstack.backend.application.port.incoming.study

import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse.EligibilityTestResponse
import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse.InformedConsentResponse
import researchstack.backend.enums.HealthDataType

interface GetParticipationRequirementUseCase {
    suspend fun getParticipationRequirement(studyId: String): ParticipationRequirementResponse
    suspend fun getParticipationRequirementForHttp(studyId: String): ParticipationRequirementResponse
    suspend fun getEligibilityTest(studyId: String): EligibilityTestResponse?
    suspend fun getInformedConsent(studyId: String): InformedConsentResponse
    suspend fun getRequiredHealthDataTypeList(studyId: String): List<HealthDataType>
}
