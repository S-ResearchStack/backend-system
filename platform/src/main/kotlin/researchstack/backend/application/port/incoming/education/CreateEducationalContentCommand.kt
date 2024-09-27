package researchstack.backend.application.port.incoming.education

import researchstack.backend.enums.EducationalContentStatus
import researchstack.backend.enums.EducationalContentType

data class CreateEducationalContentCommand(
    val title: String,
    val type: EducationalContentType,
    val status: EducationalContentStatus,
    val category: String,
    val content: Map<String, Any>
)
