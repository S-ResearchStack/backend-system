package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.education.EducationalContentEntity
import researchstack.backend.domain.education.EducationalContent

@Mapper
abstract class EducationalContentMapper {
    abstract fun toEntity(educationalContent: EducationalContent): EducationalContentEntity

    abstract fun toDomain(educationalContentEntity: EducationalContentEntity): EducationalContent

    fun mapContent(content: EducationalContent.Content): EducationalContentEntity.Content {
        return when (content) {
            is EducationalContent.ScratchContent -> toEntity(content)
            is EducationalContent.PdfContent -> toEntity(content)
            is EducationalContent.VideoContent -> toEntity(content)
        }
    }

    abstract fun toEntity(scratchContent: EducationalContent.ScratchContent): EducationalContentEntity.ScratchContent

    abstract fun toEntity(pdfContent: EducationalContent.PdfContent): EducationalContentEntity.PdfContent

    abstract fun toEntity(videoContent: EducationalContent.VideoContent): EducationalContentEntity.VideoContent

    fun mapContent(content: EducationalContentEntity.Content): EducationalContent.Content {
        return when (content) {
            is EducationalContentEntity.ScratchContent -> toDomain(content)
            is EducationalContentEntity.PdfContent -> toDomain(content)
            is EducationalContentEntity.VideoContent -> toDomain(content)
        }
    }

    abstract fun toDomain(scratchContent: EducationalContentEntity.ScratchContent): EducationalContent.ScratchContent

    abstract fun toDomain(pdfContent: EducationalContentEntity.PdfContent): EducationalContent.PdfContent

    abstract fun toDomain(videoContent: EducationalContentEntity.VideoContent): EducationalContent.VideoContent

    fun mapContentBlockList(items: List<EducationalContent.ScratchContent.Block>): List<EducationalContentEntity.ScratchContent.Block> {
        return items.map {
            when (it) {
                is EducationalContent.ScratchContent.TextBlock -> toEntity(it)
                is EducationalContent.ScratchContent.ImageBlock -> toEntity(it)
                is EducationalContent.ScratchContent.VideoBlock -> toEntity(it)
            }
        }
    }

    abstract fun toEntity(textContentBlock: EducationalContent.ScratchContent.TextBlock): EducationalContentEntity.ScratchContent.TextBlock

    abstract fun toEntity(imageContentBlock: EducationalContent.ScratchContent.ImageBlock): EducationalContentEntity.ScratchContent.ImageBlock

    abstract fun toEntity(videoContentBlock: EducationalContent.ScratchContent.VideoBlock): EducationalContentEntity.ScratchContent.VideoBlock

    fun mapContentBlockEntityList(items: List<EducationalContentEntity.ScratchContent.Block>): List<EducationalContent.ScratchContent.Block> {
        return items.map {
            when (it) {
                is EducationalContentEntity.ScratchContent.TextBlock -> toDomain(it)
                is EducationalContentEntity.ScratchContent.ImageBlock -> toDomain(it)
                is EducationalContentEntity.ScratchContent.VideoBlock -> toDomain(it)
                else -> throw IllegalArgumentException("unsupported exception")
            }
        }
    }

    abstract fun toDomain(textContentBlock: EducationalContentEntity.ScratchContent.TextBlock): EducationalContent.ScratchContent.TextBlock

    abstract fun toDomain(imageContentBlock: EducationalContentEntity.ScratchContent.ImageBlock): EducationalContent.ScratchContent.ImageBlock

    abstract fun toDomain(videoContentBlock: EducationalContentEntity.ScratchContent.VideoBlock): EducationalContent.ScratchContent.VideoBlock
}

private val converter = Mappers.getMapper(EducationalContentMapper::class.java)

fun EducationalContent.toEntity(): EducationalContentEntity =
    converter.toEntity(this)

fun EducationalContentEntity.toDomain(): EducationalContent =
    converter.toDomain(this)
