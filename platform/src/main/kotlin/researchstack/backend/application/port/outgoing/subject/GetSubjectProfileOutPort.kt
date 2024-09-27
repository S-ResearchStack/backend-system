package researchstack.backend.application.port.outgoing.subject

import researchstack.backend.domain.subject.Subject

interface GetSubjectProfileOutPort {
    suspend fun getSubjectNumber(studyId: String, subjectId: String): String
    suspend fun getSubjectProfile(id: Subject.SubjectId): Subject
}
