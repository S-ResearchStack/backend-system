package researchstack.backend.adapter.outgoing.mongo.task

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.TaskSpecRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.task.CreateTaskSpecOutPort
import researchstack.backend.domain.task.TaskSpec

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class CreateTaskSpecMongoAdapter(
    private val taskSpecRepository: TaskSpecRepository
) : CreateTaskSpecOutPort {
    override suspend fun createTaskSpec(taskSpec: TaskSpec) {
        taskSpecRepository
            .existsById(taskSpec.id)
            .flatMap {
                if (it) {
                    Mono.error(AlreadyExistsException("task id (${taskSpec.id}) already exists"))
                } else {
                    taskSpecRepository.save(taskSpec.toEntity())
                }
            }
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
    }
}
