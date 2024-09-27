package researchstack.backend.adapter.outgoing.mongo.subject

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectRepository
import researchstack.backend.application.port.outgoing.subject.DeregisterSubjectOutPort
import researchstack.backend.domain.subject.Subject

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class DeregisterSubjectMongoAdapter(
    private val subjectRepository: SubjectRepository
) : DeregisterSubjectOutPort {
    override suspend fun deregisterSubject(id: Subject.SubjectId) {
        subjectRepository.deleteById(id.value).awaitFirstOrDefault(null)
    }
}
