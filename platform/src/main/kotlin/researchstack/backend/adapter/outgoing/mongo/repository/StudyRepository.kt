package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.study.StudyEntity
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage

interface StudyRepository : ReactiveMongoRepository<StudyEntity, String> {
    fun existsByIdAndParticipationCode(id: String, participationCode: String?): Mono<Boolean>
    fun findByStudyInfoStage(stage: StudyStage): Flux<StudyEntity>

    fun findByParticipationCodeAndStudyInfoStage(participationCode: String, stage: StudyStage): Mono<StudyEntity>

    fun findByStudyInfoScopeAndStudyInfoStage(scope: StudyScope, stage: StudyStage): Flux<StudyEntity>
}
