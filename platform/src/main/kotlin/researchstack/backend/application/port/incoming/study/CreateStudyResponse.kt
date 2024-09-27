package researchstack.backend.application.port.incoming.study

import researchstack.backend.adapter.exception.ExceptionMessage

data class CreateStudyResponse(
    val id: String?
) {
    init {
        require(!id.isNullOrBlank()) { ExceptionMessage.EMPTY_STUDY_ID }
    }
}
