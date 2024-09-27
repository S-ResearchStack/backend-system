package researchstack.backend.adapter.outgoing.mongo.study.participationrequirement

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.ParticipationRequirementRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.study.CreateParticipationRequirementOutPort
import researchstack.backend.domain.study.ParticipationRequirement

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class CreateParticipationRequirementMongoAdapter(
    private val participationRequirementRepository: ParticipationRequirementRepository
) : CreateParticipationRequirementOutPort {
    override suspend fun create(requirement: ParticipationRequirement, studyId: String) {
        participationRequirementRepository
            .save(requirement.toEntity(studyId))
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
    }
}
