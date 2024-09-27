package researchstack.backend.application.port.incoming.studydata

import researchstack.backend.domain.task.TaskSpecSummary
import researchstack.backend.enums.SubjectStatus
import java.time.LocalDateTime

data class SubjectInfoResponse(
    val studyId: String,
    val subjectNumber: String,
    val status: SubjectStatus,
    val subjectId: String,
    val lastSyncTime: LocalDateTime?,
    val totalTaskCount: Number?,
    val undoneTaskList: List<TaskSpecSummary>?
)
