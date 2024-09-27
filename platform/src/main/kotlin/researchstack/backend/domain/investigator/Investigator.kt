package researchstack.backend.domain.investigator

import researchstack.backend.domain.common.Email

data class Investigator(
    val id: String,
    val email: Email,
    val firstName: String,
    val lastName: String,
    val company: String,
    val team: String,
    val officePhoneNumber: String,
    val mobilePhoneNumber: String,
    val attachmentUrls: List<String>? = null,
    var roles: List<String>? = null
) {
    init {
        require(id.isNotBlank())
    }
}
