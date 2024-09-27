package researchstack.backend.application.port.incoming.task

import researchstack.backend.domain.subject.Subject

interface CreateTaskResultUseCase {
    suspend fun createTaskResult(subjectId: Subject.SubjectId, studyId: String, taskResultCommand: TaskResultCommand)
}
