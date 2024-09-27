package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.healthdata.HealthDataGroupEntity

interface HealthDataGroupRepository : ReactiveMongoRepository<HealthDataGroupEntity, String> {
    fun findByName(name: String): Mono<HealthDataGroupEntity>
}
