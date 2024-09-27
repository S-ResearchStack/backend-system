package researchstack.backend.application.port.incoming.subject

import researchstack.backend.domain.subject.Subject.SubjectId

interface DeregisterSubjectUseCase {
    suspend fun deregisterSubject(id: SubjectId)
}
