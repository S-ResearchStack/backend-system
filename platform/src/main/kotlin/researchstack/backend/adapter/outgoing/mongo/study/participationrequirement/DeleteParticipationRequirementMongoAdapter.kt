package researchstack.backend.adapter.outgoing.mongo.study.participationrequirement

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.ParticipationRequirementRepository
import researchstack.backend.application.port.outgoing.study.DeleteParticipationRequirementOutPort

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class DeleteParticipationRequirementMongoAdapter(
    private val participationRequirementRepository: ParticipationRequirementRepository
) : DeleteParticipationRequirementOutPort {
    override suspend fun delete(studyId: String) {
        participationRequirementRepository.deleteById(studyId).awaitFirstOrDefault(null)
    }
}
