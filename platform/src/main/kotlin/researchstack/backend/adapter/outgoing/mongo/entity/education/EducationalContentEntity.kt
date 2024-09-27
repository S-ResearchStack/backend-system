package researchstack.backend.adapter.outgoing.mongo.entity.education

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import researchstack.backend.enums.EducationalContentStatus
import researchstack.backend.enums.EducationalContentType
import researchstack.backend.enums.ScratchContentBlockType
import java.time.LocalDateTime

@Document("educationalContent")
data class EducationalContentEntity(
    @Id
    val id: String? = null,
    val title: String,
    val type: EducationalContentType,
    val status: EducationalContentStatus,
    val category: String,
    val creatorId: String,
    val publisherId: String? = null,
    val modifierId: String? = null,
    val modifiedAt: LocalDateTime? = null,
    val publishedAt: LocalDateTime? = null,
    val content: Content
) {
    sealed interface Content

    data class PdfContent(
        val url: String,
        val description: String
    ) : Content

    data class VideoContent(
        val url: String,
        val description: String
    ) : Content

    data class ScratchContent(
        val coverImage: String,
        val description: String,
        val blocks: List<Block>
    ) : Content {
        interface Block {
            val id: String
            val type: ScratchContentBlockType
            val sequence: Int
        }

        data class TextBlock(
            override val id: String,
            override val type: ScratchContentBlockType,
            override val sequence: Int,
            val text: String
        ) : Block

        data class VideoBlock(
            override val id: String,
            override val type: ScratchContentBlockType,
            override val sequence: Int,
            val url: String,
            val text: String
        ) : Block

        data class ImageBlock(
            override val id: String,
            override val type: ScratchContentBlockType,
            override val sequence: Int,
            val images: List<Image>
        ) : Block

        data class Image(
            val id: String,
            val url: String,
            val caption: String
        )
    }
}
