package researchstack.backend.adapter.outgoing.mongo.task

import com.mongodb.DuplicateKeyException
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.adapter.outgoing.mongo.repository.TaskResultRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.task.CreateTaskResultOutPort
import researchstack.backend.domain.task.TaskResult

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class CreateTaskResultMongoAdapter(
    private val subjectInfoRepository: SubjectInfoRepository,
    private val taskResultRepository: TaskResultRepository
) : CreateTaskResultOutPort {
    override suspend fun createTaskResult(subjectId: String, taskResult: TaskResult) {
        subjectInfoRepository
            .findByStudyIdAndSubjectId(taskResult.studyId, subjectId)
            .awaitSingle()

        taskResultRepository
            .existsByStudyIdAndTaskIdAndSubjectId(
                studyId = taskResult.studyId,
                taskId = taskResult.taskId,
                subjectId = subjectId
            )
            .flatMap {
                if (it) {
                    Mono.error(
                        AlreadyExistsException(
                            "task result (" +
                                "studyId: ${taskResult.studyId}, " +
                                "taskId: ${taskResult.taskId}, " +
                                "subjectId: $subjectId" +
                                ") already exists"
                        )
                    )
                } else {
                    taskResultRepository.save(taskResult.toEntity(subjectId))
                }
            }
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
    }
}
