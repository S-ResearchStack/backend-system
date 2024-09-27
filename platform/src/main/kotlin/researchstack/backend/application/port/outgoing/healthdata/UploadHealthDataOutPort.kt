package researchstack.backend.application.port.outgoing.healthdata

import researchstack.backend.domain.healthdata.BatchHealthData
import researchstack.backend.domain.healthdata.HealthData
import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.HealthDataType

interface UploadHealthDataOutPort {
    suspend fun upload(
        subjectId: Subject.SubjectId,
        studyIds: List<String>,
        type: HealthDataType,
        data: List<HealthData>
    )

    suspend fun uploadBatch(subjectId: Subject.SubjectId, studyIds: List<String>, batchData: List<BatchHealthData>)
}
