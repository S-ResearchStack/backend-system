package researchstack.backend.adapter.outgoing.mongo.subject

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectRepository
import researchstack.backend.application.port.outgoing.subject.UpdateSubjectProfileOutPort
import researchstack.backend.domain.subject.Subject

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class UpdateSubjectProfileMongoAdapter(
    private val subjectRepository: SubjectRepository
) : UpdateSubjectProfileOutPort {
    override suspend fun updateSubjectProfile(id: String, subject: Subject) {
        subjectRepository.save(subject.toEntity()).awaitSingle()
    }
}
