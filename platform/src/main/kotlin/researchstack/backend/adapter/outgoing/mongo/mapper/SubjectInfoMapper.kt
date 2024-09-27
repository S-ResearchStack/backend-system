package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.SubjectInfoEntity
import researchstack.backend.domain.subject.SubjectInfo

@Mapper
abstract class SubjectInfoMapper {
    @Mapping(target = "id", ignore = true)
    abstract fun toEntity(subjectInfo: SubjectInfo): SubjectInfoEntity

    abstract fun toDomain(subjectInfoEntity: SubjectInfoEntity): SubjectInfo
}

private val converter = Mappers.getMapper(SubjectInfoMapper::class.java)

fun SubjectInfo.toEntity(): SubjectInfoEntity =
    converter.toEntity(this)

fun SubjectInfoEntity.toDomain(): SubjectInfo =
    converter.toDomain(this)
