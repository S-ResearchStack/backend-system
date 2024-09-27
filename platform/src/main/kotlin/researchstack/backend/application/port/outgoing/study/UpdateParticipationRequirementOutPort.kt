package researchstack.backend.application.port.outgoing.study

import researchstack.backend.domain.study.ParticipationRequirement

interface UpdateParticipationRequirementOutPort {
    suspend fun update(studyId: String, requirement: ParticipationRequirement)
}
