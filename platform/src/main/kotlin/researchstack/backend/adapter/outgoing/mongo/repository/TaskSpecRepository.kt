package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.task.TaskSpecEntity
import researchstack.backend.enums.TaskStatus

interface TaskSpecRepository : ReactiveMongoRepository<TaskSpecEntity, String> {
    fun findByIdAndStudyId(id: String, studyId: String): Mono<TaskSpecEntity>

    fun findByStudyIdAndStatus(studyId: String, taskStatus: TaskStatus): Flux<TaskSpecEntity>

    fun deleteByIdAndStudyId(id: String, studyId: String): Mono<Void>
}
