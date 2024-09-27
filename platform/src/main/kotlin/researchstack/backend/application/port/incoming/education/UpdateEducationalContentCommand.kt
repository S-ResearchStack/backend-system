package researchstack.backend.application.port.incoming.education

import researchstack.backend.enums.EducationalContentStatus
import researchstack.backend.enums.EducationalContentType
import java.time.LocalDateTime

data class UpdateEducationalContentCommand(
    val title: String? = null,
    val type: EducationalContentType? = null,
    val status: EducationalContentStatus? = null,
    val category: String? = null,
    val publisherId: String? = null,
    val modifierId: String? = null,
    val publishedAt: LocalDateTime? = null,
    val modifiedAt: LocalDateTime? = null,
    val content: Map<String, Any>? = null
)
