package researchstack.backend.adapter.outgoing.mongo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("subject")
data class SubjectEntity(
    @Id
    val subjectId: String,
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
)
