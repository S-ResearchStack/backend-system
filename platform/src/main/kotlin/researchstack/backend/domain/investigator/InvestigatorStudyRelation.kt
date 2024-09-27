package researchstack.backend.domain.investigator

import researchstack.backend.domain.common.Email

class InvestigatorStudyRelation(
    val email: Email,
    val studyId: String,
    val role: String
) {
    init {
        require(studyId.isNotBlank())
    }
}
