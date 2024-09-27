package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.SubjectInfoEntity

interface SubjectInfoRepository : ReactiveMongoRepository<SubjectInfoEntity, String> {
    fun existsByStudyIdAndSubjectNumber(studyId: String, subjectNumber: String): Mono<Boolean>
    fun findByStudyIdAndSubjectNumber(studyId: String, subjectNumber: String): Mono<SubjectInfoEntity>
    fun findByStudyIdAndSubjectId(studyId: String, subjectId: String): Mono<SubjectInfoEntity>
    fun deleteByStudyIdAndSubjectId(studyId: String, subjectId: String): Mono<Void>
}
