package researchstack.backend.application.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.education.CreateEducationalContentCommand
import researchstack.backend.application.port.incoming.education.UpdateEducationalContentCommand
import researchstack.backend.domain.education.EducationalContent
import researchstack.backend.enums.EducationalContentType
import researchstack.backend.util.toInstance

@Mapper
abstract class EducationalContentMapper {
    @Mapping(target = "content", source = "command")
    abstract fun toDomain(command: CreateEducationalContentCommand, creatorId: String): EducationalContent

    fun mapContent(command: CreateEducationalContentCommand): EducationalContent.Content =
        command.toDomainContent()
}

private val converter = Mappers.getMapper(EducationalContentMapper::class.java)

fun CreateEducationalContentCommand.toDomain(creatorId: String): EducationalContent =
    converter.toDomain(this, creatorId)

fun CreateEducationalContentCommand.toDomainContent(): EducationalContent.Content =
    toDomainContent(type, content)

fun UpdateEducationalContentCommand.toDomainContent(): EducationalContent.Content? =
    if (type != null && content != null) {
        toDomainContent(type, content)
    } else {
        null
    }

private fun toDomainContent(type: EducationalContentType, content: Map<String, Any>): EducationalContent.Content {
    return when (type) {
        EducationalContentType.SCRATCH -> content.toInstance<EducationalContent.ScratchContent>()
        EducationalContentType.PDF -> content.toInstance<EducationalContent.PdfContent>()
        EducationalContentType.VIDEO -> content.toInstance<EducationalContent.VideoContent>()
    }
}
