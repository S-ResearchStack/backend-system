package researchstack.backend.application.port.incoming.education

import researchstack.backend.adapter.exception.ExceptionMessage

data class CreateEducationalContentResponse(
    val id: String?
) {
    init {
        require(!id.isNullOrBlank()) { ExceptionMessage.EMPTY_EDUCATIONAL_CONTENT_ID }
    }
}
