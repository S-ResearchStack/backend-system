package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.StudyDataFileEntity
import researchstack.backend.enums.StudyDataType

interface StudyDataFileRepository : ReactiveMongoRepository<StudyDataFileEntity, String> {
    fun findAllByStudyIdAndParentIdAndType(
        studyId: String,
        parentId: String,
        type: StudyDataType
    ): Flux<StudyDataFileEntity>
}
