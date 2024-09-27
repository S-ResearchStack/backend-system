package researchstack.backend.application.port.outgoing.subject

import researchstack.backend.domain.subject.Subject

interface UpdateSubjectProfileOutPort {
    suspend fun updateSubjectProfile(id: String, subject: Subject)
}
