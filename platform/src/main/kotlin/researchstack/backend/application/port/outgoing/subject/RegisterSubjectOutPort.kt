package researchstack.backend.application.port.outgoing.subject

import researchstack.backend.domain.subject.Subject

interface RegisterSubjectOutPort {
    suspend fun registerSubject(subject: Subject)
}
