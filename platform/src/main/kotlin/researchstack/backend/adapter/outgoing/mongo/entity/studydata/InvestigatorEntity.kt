package researchstack.backend.adapter.outgoing.mongo.entity.studydata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("investigator")
data class InvestigatorEntity(
    @Id
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val company: String,
    val team: String,
    val officePhoneNumber: String,
    val mobilePhoneNumber: String,
    val attachmentUrls: List<String>? = null
)
