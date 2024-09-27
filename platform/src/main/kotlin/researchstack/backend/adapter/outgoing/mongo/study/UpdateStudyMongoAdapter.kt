package researchstack.backend.adapter.outgoing.mongo.study

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.StudyRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.study.UpdateStudyOutPort
import researchstack.backend.domain.study.Study

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class UpdateStudyMongoAdapter(
    private val studyRepository: StudyRepository
) : UpdateStudyOutPort {
    override suspend fun updateStudy(study: Study) {
        studyRepository.findById(study.id)
            .flatMap {
                studyRepository.save(study.toEntity())
            }
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
    }
}
