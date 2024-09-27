package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.task.TaskResultEntity

interface TaskResultRepository : ReactiveMongoRepository<TaskResultEntity, String> {
    fun findByStudyIdAndSubjectIdAndStartedAtGreaterThanAndFinishedAtLessThan(
        studyId: String,
        subjectId: String,
        startedAt: Long,
        finishedAt: Long
    ): Flux<TaskResultEntity>

    fun existsByStudyIdAndTaskIdAndSubjectId(studyId: String, taskId: String, subjectId: String): Mono<Boolean>
}
