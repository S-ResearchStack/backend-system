package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.StudyDataFolderEntity
import researchstack.backend.enums.StudyDataType

interface StudyDataFolderRepository : ReactiveMongoRepository<StudyDataFolderEntity, String> {
    fun findAllByStudyIdAndParentIdAndType(
        studyId: String,
        parentId: String,
        type: StudyDataType
    ): Flux<StudyDataFolderEntity>

    fun existsByStudyIdAndParentIdAndName(studyId: String, parentId: String?, name: String): Mono<Boolean>
    fun findByStudyIdAndParentIdAndName(studyId: String, parentId: String, name: String): Mono<StudyDataFolderEntity>
}
