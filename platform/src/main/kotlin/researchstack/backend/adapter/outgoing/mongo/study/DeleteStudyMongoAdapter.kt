package researchstack.backend.adapter.outgoing.mongo.study

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.StudyRepository
import researchstack.backend.application.port.outgoing.study.DeleteStudyOutPort

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class DeleteStudyMongoAdapter(
    private val studyRepository: StudyRepository
) : DeleteStudyOutPort {
    override suspend fun deleteStudy(studyId: String) {
        studyRepository.deleteById(studyId).awaitFirstOrDefault(null)
    }
}
