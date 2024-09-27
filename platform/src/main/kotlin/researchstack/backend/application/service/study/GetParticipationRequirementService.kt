package researchstack.backend.application.service.study

import org.springframework.stereotype.Service
import researchstack.backend.adapter.incoming.mapper.study.toResponse
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.study.GetParticipationRequirementUseCase
import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse
import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse.EligibilityTestResponse
import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse.InformedConsentResponse
import researchstack.backend.application.port.outgoing.study.GetParticipationRequirementOutPort
import researchstack.backend.enums.HealthDataType

@Service
class GetParticipationRequirementService(
    private val outPort: GetParticipationRequirementOutPort
) : GetParticipationRequirementUseCase {
    override suspend fun getParticipationRequirement(@Tenants studyId: String): ParticipationRequirementResponse {
        return getParticipationRequirementInternal(studyId)
    }

    override suspend fun getParticipationRequirementForHttp(@Tenants studyId: String): ParticipationRequirementResponse {
        return getParticipationRequirementInternal(studyId)
    }

    override suspend fun getEligibilityTest(@Tenants studyId: String): EligibilityTestResponse? {
        val eligibilityTest = outPort.getEligibilityTest(studyId)
        return eligibilityTest?.toResponse()
    }

    override suspend fun getInformedConsent(@Tenants studyId: String): InformedConsentResponse {
        val informedConsent = outPort.getInformedConsent(studyId)
        return informedConsent.toResponse()
    }

    override suspend fun getRequiredHealthDataTypeList(@Tenants studyId: String): List<HealthDataType> {
        return outPort.getRequiredHealthDataTypeList(studyId)
    }

    private suspend fun getParticipationRequirementInternal(studyId: String): ParticipationRequirementResponse {
        val participationRequirement = outPort.getParticipationRequirement(studyId)
        return ParticipationRequirementResponse(
            eligibilityTest = participationRequirement.eligibilityTest?.toResponse(),
            informedConsent = participationRequirement.informedConsent.toResponse(),
            healthDataTypeList = participationRequirement.healthDataTypeList,
            taskInfo = participationRequirement.taskInfo?.map {
                it.toResponse()
            }
        )
    }
}
