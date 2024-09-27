package researchstack.backend.application.port.incoming.studydata

import researchstack.backend.enums.SubjectStatus

interface UpdateSubjectUseCase {
    suspend fun updateSubjectStatus(studyId: String, subjectNumber: String, status: SubjectStatus)
}
