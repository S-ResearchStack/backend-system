package researchstack.backend.application.port.outgoing.study

import researchstack.backend.domain.study.ParticipationRequirement

interface CreateParticipationRequirementOutPort {
    suspend fun create(requirement: ParticipationRequirement, studyId: String)
}
