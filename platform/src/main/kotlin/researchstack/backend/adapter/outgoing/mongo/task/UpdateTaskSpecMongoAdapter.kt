package researchstack.backend.adapter.outgoing.mongo.task

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.TaskSpecRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.task.UpdateTaskSpecOutPort
import researchstack.backend.domain.task.TaskSpec

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class UpdateTaskSpecMongoAdapter(
    private val taskSpecRepository: TaskSpecRepository
) : UpdateTaskSpecOutPort {
    override suspend fun updateTaskSpec(taskSpec: TaskSpec) {
        taskSpecRepository
            .findByIdAndStudyId(taskSpec.id, taskSpec.studyId)
            .flatMap {
                taskSpecRepository.save(taskSpec.toEntity())
            }
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
    }
}
