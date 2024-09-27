package researchstack.backend.adapter.outgoing.mongo.task

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.TaskResultRepository
import researchstack.backend.application.port.outgoing.task.GetTaskResultOutPort
import researchstack.backend.domain.task.TaskResult
import researchstack.backend.util.toLong
import java.time.LocalDateTime

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class GetTaskResultMongoAdapter(
    private val taskResultRepository: TaskResultRepository
) : GetTaskResultOutPort {
    override suspend fun getTaskResultList(
        studyId: String,
        subjectId: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<TaskResult> {
        return taskResultRepository
            .findByStudyIdAndSubjectIdAndStartedAtGreaterThanAndFinishedAtLessThan(
                studyId,
                subjectId,
                startTime.toLong(),
                endTime.toLong()
            )
            .map { it.toDomain() }
            .collectList()
            .awaitSingle()
    }
}
