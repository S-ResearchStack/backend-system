package researchstack.backend.adapter.outgoing.mongo.studydata

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.application.port.outgoing.studydata.UpdateSubjectInfoOutPort
import researchstack.backend.enums.SubjectStatus

@Component
class UpdateSubjectInfoMongoAdapter(
    private val subjectInfoRepository: SubjectInfoRepository
) : UpdateSubjectInfoOutPort {
    override suspend fun updateSubjectStatus(
        studyId: String,
        subjectNumber: String,
        status: SubjectStatus,
        subjectId: String
    ) {
        val subjectInfo = subjectInfoRepository.findByStudyIdAndSubjectNumber(studyId, subjectNumber)
            .awaitSingle()
        subjectInfo.status = status
        subjectInfoRepository.save(subjectInfo).awaitSingle()
    }
}
