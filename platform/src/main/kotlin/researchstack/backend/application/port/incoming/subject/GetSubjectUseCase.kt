package researchstack.backend.application.port.incoming.subject

import researchstack.backend.domain.subject.Subject
import researchstack.backend.domain.subject.Subject.SubjectId
import researchstack.backend.domain.subject.SubjectStatusInfo

interface GetSubjectUseCase {
    suspend fun getSubjectProfile(id: SubjectId): Subject
    suspend fun getSubjectStatus(subjectId: SubjectId, studyId: String): SubjectStatusInfo
}
