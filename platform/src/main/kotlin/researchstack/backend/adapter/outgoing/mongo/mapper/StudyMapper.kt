package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.study.StudyEntity
import researchstack.backend.domain.study.Study

@Mapper
abstract class StudyMapper {
    abstract fun toEntity(study: Study): StudyEntity
    abstract fun toDomain(studyEntity: StudyEntity): Study
}

private val converter = Mappers.getMapper(StudyMapper::class.java)

fun Study.toEntity(): StudyEntity = converter.toEntity(this)

fun StudyEntity.toDomain(): Study = converter.toDomain(this)
