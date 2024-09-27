package researchstack.backend.adapter.outgoing.mongo.study.studyrelation

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectStudyRelationRepository
import researchstack.backend.application.port.outgoing.study.DeleteSubjectStudyRelationOutPort

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class DeleteSubjectStudyRelationMongoAdapter(
    private val studyRelationRepository: SubjectStudyRelationRepository
) : DeleteSubjectStudyRelationOutPort {
    override suspend fun deleteSubjectStudyRelation(subjectId: String, studyId: String) {
        studyRelationRepository.deleteBySubjectIdAndStudyId(subjectId, studyId).awaitFirstOrDefault(null)
    }
}
