package researchstack.backend.application.port.incoming.healthdata

import researchstack.backend.domain.subject.Subject

interface UploadHealthDataUseCase {
    suspend fun upload(subjectId: Subject.SubjectId, studyIds: List<String>, command: UploadHealthDataCommand)

    suspend fun uploadBatch(subjectId: Subject.SubjectId, studyIds: List<String>, command: UploadBatchHealthDataCommand)
}
