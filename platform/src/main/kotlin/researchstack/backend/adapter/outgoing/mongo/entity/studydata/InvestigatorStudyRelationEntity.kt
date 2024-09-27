package researchstack.backend.adapter.outgoing.mongo.entity.studydata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("investigatorStudyRelation")
data class InvestigatorStudyRelationEntity(
    @Id
    val id: String?,
    val email: String,
    val studyId: String,
    var role: String
)
