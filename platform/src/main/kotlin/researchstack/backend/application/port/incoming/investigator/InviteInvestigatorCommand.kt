package researchstack.backend.application.port.incoming.investigator

import researchstack.backend.adapter.exception.ExceptionMessage

data class InviteInvestigatorCommand(
    val email: String,
    val studyId: String,
    val role: String
) {
    init {
        require(email.isNotBlank()) { ExceptionMessage.EMPTY_EMAIL }
        require(studyId.isNotBlank()) { ExceptionMessage.EMPTY_STUDY_ID }
        require(role.isNotBlank()) { ExceptionMessage.EMPTY_ROLE }
    }
}
