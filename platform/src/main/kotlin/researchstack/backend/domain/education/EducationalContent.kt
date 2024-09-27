package researchstack.backend.domain.education

import kotlinx.serialization.Serializable
import researchstack.backend.adapter.serializer.LocalDateTimeSerializer
import researchstack.backend.application.serializer.education.ScratchContentBlockPolymorphicSerializer
import researchstack.backend.enums.EducationalContentStatus
import researchstack.backend.enums.EducationalContentType
import researchstack.backend.enums.ScratchContentBlockType
import java.time.LocalDateTime

@Serializable
data class EducationalContent(
    val id: String? = null,
    val title: String,
    val type: EducationalContentType,
    val status: EducationalContentStatus,
    val category: String,
    val creatorId: String,
    val publisherId: String? = null,
    val modifierId: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val publishedAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val modifiedAt: LocalDateTime? = null,
    val content: Content
) {
    sealed interface Content

    @Serializable
    data class PdfContent(
        val url: String,
        val description: String
    ) : Content

    @Serializable
    data class VideoContent(
        val url: String,
        val description: String
    ) : Content

    @Serializable
    data class ScratchContent(
        val coverImage: String,
        val description: String,
        val blocks: List<Block>
    ) : Content {
        @Serializable(ScratchContentBlockPolymorphicSerializer::class)
        sealed interface Block {
            val id: String
            val type: ScratchContentBlockType
            val sequence: Int
        }

        @Serializable
        data class TextBlock(
            override val id: String,
            override val type: ScratchContentBlockType,
            override val sequence: Int,
            val text: String
        ) : Block

        @Serializable
        data class VideoBlock(
            override val id: String,
            override val type: ScratchContentBlockType,
            override val sequence: Int,
            val url: String,
            val text: String
        ) : Block

        @Serializable
        data class ImageBlock(
            override val id: String,
            override val type: ScratchContentBlockType,
            override val sequence: Int,
            val images: List<Image>
        ) : Block

        @Serializable
        data class Image(
            val id: String,
            val url: String,
            val caption: String
        )
    }

    fun new(
        id: String? = null,
        title: String? = null,
        type: EducationalContentType? = null,
        status: EducationalContentStatus? = null,
        category: String? = null,
        creatorId: String? = null,
        publisherId: String? = null,
        modifierId: String? = null,
        publishedAt: LocalDateTime? = null,
        modifiedAt: LocalDateTime? = null,
        content: Content? = null
    ): EducationalContent = EducationalContent(
        id = id ?: this.id,
        title = title ?: this.title,
        type = type ?: this.type,
        status = status ?: this.status,
        category = category ?: this.category,
        creatorId = creatorId ?: this.creatorId,
        publisherId = publisherId ?: this.publisherId,
        modifierId = modifierId ?: this.modifierId,
        publishedAt = publishedAt ?: this.publishedAt,
        modifiedAt = modifiedAt ?: this.modifiedAt,
        content = content ?: this.content
    )
}
