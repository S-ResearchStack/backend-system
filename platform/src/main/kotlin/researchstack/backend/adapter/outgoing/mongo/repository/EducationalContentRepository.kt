package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.education.EducationalContentEntity
import researchstack.backend.enums.EducationalContentStatus

interface EducationalContentRepository : ReactiveMongoRepository<EducationalContentEntity, String> {
    fun findByStatus(status: EducationalContentStatus): Flux<EducationalContentEntity>
    fun existsByTitle(title: String): Mono<Boolean>
}
