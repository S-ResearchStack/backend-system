package researchstack.backend.adapter.outgoing.mongo.task

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.TaskSpecRepository
import researchstack.backend.application.port.outgoing.study.GetStudyOutPort
import researchstack.backend.application.port.outgoing.task.GetTaskSpecOutPort
import researchstack.backend.domain.subject.Subject
import researchstack.backend.domain.task.TaskSpec
import researchstack.backend.enums.TaskStatus
import java.time.LocalDateTime

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class GetTaskSpecMongoAdapter(
    @Qualifier("getStudyMongoAdapter")
    private val getStudyOutPort: GetStudyOutPort,
    private val taskSpecRepository: TaskSpecRepository
) : GetTaskSpecOutPort {
    override suspend fun getTaskSpec(taskId: String): TaskSpec {
        return taskSpecRepository
            .findById(taskId)
            .map {
                it.toDomain()
            }
            .awaitSingle()
    }

    override suspend fun getTaskSpecs(studyId: String, status: TaskStatus?): List<TaskSpec> {
        return taskSpecRepository
            .findByStudyIdAndStatus(studyId, status ?: TaskStatus.PUBLISHED)
            .map { it.toDomain() }
            .collectList()
            .awaitSingle()
    }

    override suspend fun getAllTaskSpecs(subjectId: Subject.SubjectId): List<TaskSpec> {
        return getStudyOutPort
            .getParticipatedStudyList(subjectId.value)
            .map {
                getTaskSpecs(it.id)
            }
            .flatten()
    }

    override suspend fun getAllNewTaskSpecs(subjectId: Subject.SubjectId, lastSyncTime: LocalDateTime): List<TaskSpec> {
        return getAllTaskSpecs(subjectId)
            .filter { taskSpec ->
                taskSpec.publishedAt?.isAfter(lastSyncTime) ?: false
            }
    }
}
