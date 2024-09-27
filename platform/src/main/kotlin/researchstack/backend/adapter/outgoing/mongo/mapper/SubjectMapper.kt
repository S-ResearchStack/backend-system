package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.SubjectEntity
import researchstack.backend.domain.subject.Subject

@Mapper
abstract class SubjectMapper {
    abstract fun toEntity(subject: Subject): SubjectEntity

    fun mapSubjectId(subjectId: Subject.SubjectId): String = subjectId.value

    abstract fun toDomain(subjectEntity: SubjectEntity): Subject

    fun mapSubjectId(subjectId: String): Subject.SubjectId = Subject.SubjectId.from(subjectId)
}

private val converter = Mappers.getMapper(SubjectMapper::class.java)

fun Subject.toEntity(): SubjectEntity =
    converter.toEntity(this)

fun SubjectEntity.toDomain(): Subject =
    converter.toDomain(this)
