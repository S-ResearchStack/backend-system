package researchstack.backend.adapter.outgoing.mongo.subject

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectRepository
import researchstack.backend.application.port.outgoing.subject.GetSubjectProfileOutPort
import researchstack.backend.domain.subject.Subject

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class GetSubjectProfileMongoAdapter(
    private val subjectRepository: SubjectRepository,
    private val subjectInfoRepository: SubjectInfoRepository
) : GetSubjectProfileOutPort {
    override suspend fun getSubjectNumber(studyId: String, subjectId: String): String {
        return subjectInfoRepository.findByStudyIdAndSubjectId(studyId, subjectId).awaitSingle().subjectNumber
    }

    override suspend fun getSubjectProfile(id: Subject.SubjectId): Subject {
        return subjectRepository.findById(id.value).awaitSingle().toDomain()
    }
}
