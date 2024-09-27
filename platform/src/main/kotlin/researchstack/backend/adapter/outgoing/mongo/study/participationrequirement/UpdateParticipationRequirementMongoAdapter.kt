package researchstack.backend.adapter.outgoing.mongo.study.participationrequirement

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.ParticipationRequirementRepository
import researchstack.backend.application.port.outgoing.study.UpdateParticipationRequirementOutPort
import researchstack.backend.domain.study.ParticipationRequirement

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class UpdateParticipationRequirementMongoAdapter(
    private val participationRequirementRepository: ParticipationRequirementRepository
) : UpdateParticipationRequirementOutPort {
    override suspend fun update(studyId: String, requirement: ParticipationRequirement) {
        participationRequirementRepository.findById(studyId)
            .flatMap {
                participationRequirementRepository.save(requirement.toEntity(studyId))
            }
            .awaitSingle()
    }
}
