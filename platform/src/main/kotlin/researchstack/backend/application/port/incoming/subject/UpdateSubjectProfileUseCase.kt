package researchstack.backend.application.port.incoming.subject

import researchstack.backend.domain.subject.Subject.SubjectId

interface UpdateSubjectProfileUseCase {
    suspend fun updateSubjectProfile(id: SubjectId, command: UpdateSubjectProfileCommand)
}
