package researchstack.backend.application.port.incoming.investigator

import researchstack.backend.adapter.exception.ExceptionMessage

data class DeleteInvestigatorRoleCommand(
    val studyId: String
) {
    init {
        require(studyId.isNotBlank()) { ExceptionMessage.EMPTY_STUDY_ID }
    }
}
