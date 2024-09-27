package researchstack.backend.application.port.incoming.common

import researchstack.backend.adapter.exception.ExceptionMessage

data class SendInvitationEmailCommand(
    val email: String,
    val sender: String,
    val studyName: String,
    val role: String
) {
    init {
        require(email.isNotBlank()) { ExceptionMessage.EMPTY_EMAIL }
        require(sender.isNotBlank()) { ExceptionMessage.EMPTY_SENDER }
        require(studyName.isNotBlank()) { ExceptionMessage.EMPTY_STUDY_NAME }
        require(role.isNotBlank()) { ExceptionMessage.EMPTY_ROLE }
    }
}
