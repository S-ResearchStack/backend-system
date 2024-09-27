package researchstack.backend.application.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.subject.RegisterSubjectCommand
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileCommand
import researchstack.backend.domain.subject.Subject

@Mapper(componentModel = "spring")
abstract class SubjectMapper {
    companion object {
        val INSTANCE: SubjectMapper = Mappers.getMapper(SubjectMapper::class.java)
    }

    abstract fun toDomain(command: RegisterSubjectCommand): Subject
    fun mapSubjectId(subjectId: String): Subject.SubjectId = Subject.SubjectId.from(subjectId)

    abstract fun toDomain(command: UpdateSubjectProfileCommand, subjectId: String): Subject
}
