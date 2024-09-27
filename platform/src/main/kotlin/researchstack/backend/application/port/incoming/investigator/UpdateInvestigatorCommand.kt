package researchstack.backend.application.port.incoming.investigator

data class UpdateInvestigatorCommand(
    val firstName: String,
    val lastName: String,
    val company: String,
    val team: String,
    val officePhoneNumber: String,
    val mobilePhoneNumber: String,
    val attachmentUrls: List<String>? = null
)
