package researchstack.backend.application.port.outgoing.subject

import researchstack.backend.domain.subject.Subject

interface DeregisterSubjectOutPort {
    suspend fun deregisterSubject(id: Subject.SubjectId)
}
