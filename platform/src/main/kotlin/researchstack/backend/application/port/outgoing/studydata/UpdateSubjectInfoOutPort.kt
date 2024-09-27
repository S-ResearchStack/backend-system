package researchstack.backend.application.port.outgoing.studydata

import researchstack.backend.enums.SubjectStatus

interface UpdateSubjectInfoOutPort {
    suspend fun updateSubjectStatus(
        studyId: String,
        subjectNumber: String,
        status: SubjectStatus,
        subjectId: String
    )
}
