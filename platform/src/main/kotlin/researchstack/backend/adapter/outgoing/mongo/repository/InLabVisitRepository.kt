package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.inlabvisit.InLabVisitEntity
import java.time.LocalDateTime

interface InLabVisitRepository : ReactiveMongoRepository<InLabVisitEntity, String> {
    fun existsBySubjectNumberAndStartTimeBetweenOrEndTimeBetween(
        subjectNumber: String,
        startTimeFrom: LocalDateTime,
        startTimeTo: LocalDateTime,
        endTimeFrom: LocalDateTime,
        endTimeTo: LocalDateTime
    ): Mono<Boolean>
}
