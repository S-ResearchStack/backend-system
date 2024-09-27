package researchstack.backend.application.port.incoming.investigator

import researchstack.backend.adapter.exception.ExceptionMessage

data class RegisterInvestigatorCommand(
    val firstName: String,
    val lastName: String,
    val company: String,
    val team: String,
    val officePhoneNumber: String,
    val mobilePhoneNumber: String,
    val attachmentUrls: List<String>? = null
) {
    init {
        require(firstName.isNotBlank()) { ExceptionMessage.EMPTY_FIRST_NAME }
        require(lastName.isNotBlank()) { ExceptionMessage.EMPTY_LAST_NAME }
    }
}
