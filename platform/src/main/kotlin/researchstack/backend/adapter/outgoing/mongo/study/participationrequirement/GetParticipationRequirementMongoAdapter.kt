package researchstack.backend.adapter.outgoing.mongo.study.participationrequirement

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.ParticipationRequirementRepository
import researchstack.backend.application.port.outgoing.study.GetParticipationRequirementOutPort
import researchstack.backend.domain.study.EligibilityTest
import researchstack.backend.domain.study.InformedConsent
import researchstack.backend.domain.study.ParticipationRequirement
import researchstack.backend.enums.HealthDataType

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class GetParticipationRequirementMongoAdapter(
    private val participationRequirementRepository: ParticipationRequirementRepository
) : GetParticipationRequirementOutPort {
    override suspend fun getParticipationRequirement(studyId: String): ParticipationRequirement {
        return participationRequirementRepository
            .findById(studyId)
            .map {
                it.toDomain()
            }
            .awaitSingle()
    }

    override suspend fun getEligibilityTest(studyId: String): EligibilityTest? {
        return participationRequirementRepository
            .findById(studyId)
            .mapNotNull {
                it.toDomain().eligibilityTest
            }
            .awaitSingle()
    }

    override suspend fun getInformedConsent(studyId: String): InformedConsent {
        return participationRequirementRepository
            .findById(studyId)
            .map {
                it.toDomain().informedConsent
            }
            .awaitSingle()
    }

    override suspend fun getRequiredHealthDataTypeList(studyId: String): List<HealthDataType> {
        return participationRequirementRepository
            .findById(studyId)
            .map {
                it.toDomain().healthDataTypeList
            }
            .awaitSingle()
    }
}
