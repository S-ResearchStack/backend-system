package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.InvestigatorStudyRelationEntity

interface InvestigatorStudyRelationRepository : ReactiveMongoRepository<InvestigatorStudyRelationEntity, String> {
    fun findByEmailAndStudyId(email: String, studyId: String): Mono<InvestigatorStudyRelationEntity>
    fun existsByEmailAndStudyId(email: String, studyId: String): Mono<Boolean>
    fun findAllByEmail(email: String): Flux<InvestigatorStudyRelationEntity>
    fun findAllByStudyId(studyId: String): Flux<InvestigatorStudyRelationEntity>
    fun deleteByEmailAndStudyId(email: String, studyId: String): Mono<Void>
    fun deleteByStudyId(studyId: String): Mono<Void>
}
