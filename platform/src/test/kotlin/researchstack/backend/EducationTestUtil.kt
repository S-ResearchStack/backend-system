package researchstack.backend

import researchstack.backend.adapter.outgoing.mongo.entity.education.EducationalContentEntity
import researchstack.backend.application.port.incoming.education.CreateEducationalContentCommand
import researchstack.backend.application.port.incoming.education.UpdateEducationalContentCommand
import researchstack.backend.domain.education.EducationalContent
import researchstack.backend.enums.EducationalContentStatus
import researchstack.backend.enums.EducationalContentType
import researchstack.backend.enums.ScratchContentBlockType

class EducationTestUtil {
    companion object {
        const val studyId = "testStudy"
        const val contentId = "testContent"
        const val title = "Test Education"
        val type = EducationalContentType.PDF
        val status = EducationalContentStatus.DRAFT
        const val category = "Test Category"
        const val userId = "testUser"
        const val pdfUrl = "www.example.com"
        const val pdfDescription = "test description"
        const val videoUrl = "www.example.com"
        const val videoDescription = "video description"
        const val coverImage = "example.jpg"
        const val description = "scratch description"
        const val blockId = "testBlock"
        const val blockSequence = 0
        val pdfContent = mapOf("url" to pdfUrl, "description" to pdfDescription)

        fun getCreateEducationalContentCommand(
            t: String? = null,
            c: String? = null
        ) = CreateEducationalContentCommand(
            title = t ?: title,
            type = type,
            status = status,
            category = c ?: category,
            content = pdfContent
        )

        fun getCreateEducationalContentCommand(
            contentType: EducationalContentType,
            blockType: ScratchContentBlockType = ScratchContentBlockType.TEXT
        ) = CreateEducationalContentCommand(
            title = title,
            status = status,
            category = category,
            type = contentType,
            content = when (contentType) {
                EducationalContentType.PDF -> pdfContent
                EducationalContentType.VIDEO -> pdfContent
                EducationalContentType.SCRATCH -> mapOf(
                    "coverImage" to "coverImage",
                    "description" to "description",
                    "blocks" to listOf(
                        when (blockType) {
                            ScratchContentBlockType.TEXT -> mapOf(
                                "id" to "id",
                                "type" to ScratchContentBlockType.TEXT,
                                "sequence" to 0,
                                "text" to "text"
                            )

                            ScratchContentBlockType.VIDEO -> mapOf(
                                "id" to "id",
                                "type" to ScratchContentBlockType.VIDEO,
                                "sequence" to 0,
                                "url" to "url",
                                "text" to "text"
                            )

                            ScratchContentBlockType.IMAGE -> mapOf(
                                "id" to "id",
                                "type" to ScratchContentBlockType.IMAGE,
                                "sequence" to 0,
                                "images" to listOf(
                                    mapOf(
                                        "id" to "id",
                                        "url" to "url",
                                        "caption" to "caption"
                                    )
                                )
                            )
                        }
                    )
                )
            }
        )

        fun getUpdateEducationalContentCommand() = UpdateEducationalContentCommand(
            title = title,
            type = type,
            status = status,
            category = category
        )

        fun getEducationalContent(
            id: String? = null,
            contentType: EducationalContentType = EducationalContentType.PDF,
            blockType: ScratchContentBlockType = ScratchContentBlockType.TEXT
        ) = EducationalContent(
            id = id,
            title = title,
            type = type,
            status = status,
            category = category,
            creatorId = userId,
            content = when (contentType) {
                EducationalContentType.SCRATCH -> EducationalContent.ScratchContent(
                    coverImage,
                    description,
                    listOf(
                        when (blockType) {
                            ScratchContentBlockType.TEXT -> EducationalContent.ScratchContent.TextBlock(
                                id = blockId,
                                type = ScratchContentBlockType.TEXT,
                                sequence = blockSequence,
                                text = "text"
                            )

                            ScratchContentBlockType.IMAGE -> EducationalContent.ScratchContent.ImageBlock(
                                id = blockId,
                                type = ScratchContentBlockType.IMAGE,
                                sequence = blockSequence,
                                images = listOf()
                            )

                            ScratchContentBlockType.VIDEO -> EducationalContent.ScratchContent.VideoBlock(
                                id = blockId,
                                type = ScratchContentBlockType.VIDEO,
                                sequence = blockSequence,
                                url = "url",
                                text = "text"
                            )
                        }
                    )
                )

                EducationalContentType.PDF -> EducationalContent.PdfContent(pdfUrl, pdfDescription)
                EducationalContentType.VIDEO -> EducationalContent.VideoContent(videoUrl, videoDescription)
            }
        )

        fun getEducationalContentEntity() = EducationalContentEntity(
            title = title,
            type = type,
            status = status,
            category = category,
            creatorId = userId,
            content = EducationalContentEntity.PdfContent(
                url = pdfUrl,
                description = pdfDescription
            )
        )
    }
}
