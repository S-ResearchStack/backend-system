package researchstack.backend.application.port.incoming.subject

import researchstack.backend.application.service.mapper.SubjectMapper
import researchstack.backend.domain.subject.Subject

data class RegisterSubjectCommand(
    var subjectId: String? = null,
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
    fun toDomain(): Subject = SubjectMapper.INSTANCE.toDomain(this)
}
