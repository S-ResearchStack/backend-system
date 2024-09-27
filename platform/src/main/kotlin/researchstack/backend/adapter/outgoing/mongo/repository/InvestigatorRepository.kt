package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.InvestigatorEntity

interface InvestigatorRepository : ReactiveMongoRepository<InvestigatorEntity, String> {
    fun findByEmail(email: String): Mono<InvestigatorEntity>
    fun findAllByEmailIn(investigatorEmails: List<String>): Flux<InvestigatorEntity>
}
