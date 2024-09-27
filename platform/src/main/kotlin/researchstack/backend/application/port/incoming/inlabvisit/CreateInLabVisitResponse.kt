package researchstack.backend.application.port.incoming.inlabvisit

import researchstack.backend.adapter.exception.ExceptionMessage

data class CreateInLabVisitResponse(
    val id: String?
) {
    init {
        require(!id.isNullOrBlank()) { ExceptionMessage.EMPTY_IN_LAB_VISIT_ID }
    }
}
