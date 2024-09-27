package researchstack.backend.domain.subject

import researchstack.backend.enums.SubjectStatus

data class SubjectInfo(
    val studyId: String,
    val subjectNumber: String,
    var status: SubjectStatus = SubjectStatus.PARTICIPATING,
    val subjectId: String
)
