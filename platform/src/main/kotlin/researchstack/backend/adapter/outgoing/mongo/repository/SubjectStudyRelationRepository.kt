package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.study.SubjectStudyRelationEntity

interface SubjectStudyRelationRepository : ReactiveMongoRepository<SubjectStudyRelationEntity, String> {
    fun findBySubjectId(subjectId: String): Flux<SubjectStudyRelationEntity>

    fun deleteBySubjectIdAndStudyId(subjectId: String, studyId: String): Mono<Void>
}
