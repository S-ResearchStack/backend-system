package researchstack.backend.application.port.outgoing.studydata

import researchstack.backend.domain.subject.SubjectInfo

interface AddSubjectInfoOutPort {
    suspend fun addSubjectInfo(subjectInfo: SubjectInfo): SubjectInfo
}
