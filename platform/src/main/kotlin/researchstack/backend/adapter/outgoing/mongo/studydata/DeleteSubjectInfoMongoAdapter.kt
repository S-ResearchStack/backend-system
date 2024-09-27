package researchstack.backend.adapter.outgoing.mongo.studydata

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.application.port.outgoing.studydata.DeleteSubjectInfoOutPort

@Component
class DeleteSubjectInfoMongoAdapter(
    private val subjectInfoRepository: SubjectInfoRepository
) : DeleteSubjectInfoOutPort {
    override suspend fun deleteSubjectInfo(studyId: String, subjectId: String) {
        subjectInfoRepository.deleteByStudyIdAndSubjectId(studyId, subjectId).awaitFirstOrDefault(null)
    }
}
