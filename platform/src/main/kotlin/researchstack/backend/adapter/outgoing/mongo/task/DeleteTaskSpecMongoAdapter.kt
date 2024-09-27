package researchstack.backend.adapter.outgoing.mongo.task

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.TaskSpecRepository
import researchstack.backend.application.port.outgoing.task.DeleteTaskSpecOutPort

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class DeleteTaskSpecMongoAdapter(
    private val taskSpecRepository: TaskSpecRepository
) : DeleteTaskSpecOutPort {
    override suspend fun deleteTaskSpec(taskId: String) {
        TODO("Unsecure function. Remove this function or replace it with the 'deleteTaskSpec(taskId: String, studyId: String)' function")
    }

    override suspend fun deleteTaskSpec(taskId: String, studyId: String) {
        taskSpecRepository.deleteByIdAndStudyId(taskId, studyId).awaitFirstOrDefault(null)
    }
}
