package researchstack.backend.application.port.incoming.investigator

import researchstack.backend.adapter.exception.ExceptionMessage

data class UpdateInvestigatorRoleCommand(
    val studyId: String,
    val role: String
) {
    init {
        require(studyId.isNotBlank()) { ExceptionMessage.EMPTY_STUDY_ID }
        require(role.isNotBlank()) { ExceptionMessage.EMPTY_ROLE }
    }
}
