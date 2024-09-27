package researchstack.backend.domain.subject

data class Subject(
    val subjectId: SubjectId,
    val firstName: String,
    val lastName: String,
    val birthdayYear: Int,
    val birthdayMonth: Int,
    val birthdayDay: Int,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val officePhoneNumber: String? = null,
    val company: String? = null,
    val team: String? = null
) {
    companion object {
        fun new(
            subjectId: String,
            firstName: String,
            lastName: String,
            birthdayYear: Int,
            birthdayMonth: Int,
            birthdayDay: Int,
            email: String,
            phoneNumber: String,
            address: String,
            officePhoneNumber: String? = null,
            company: String? = null,
            team: String? = null
        ): Subject =
            Subject(
                SubjectId.from(subjectId),
                firstName,
                lastName,
                birthdayYear,
                birthdayMonth,
                birthdayDay,
                email,
                phoneNumber,
                address,
                officePhoneNumber,
                company,
                team
            )
    }

    data class SubjectId private constructor(val value: String) {
        companion object {
            fun from(value: String?): SubjectId {
                requireNotNull(value)
                return SubjectId(value)
            }
        }
    }
}
