package researchstack.backend.adapter.outgoing.mongo.entity.studydata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import researchstack.backend.enums.SubjectStatus

@Document("subjectInfo")
data class SubjectInfoEntity(
    @Id
    val id: String?,
    val studyId: String,
    val subjectNumber: String,
    var status: SubjectStatus?,
    val subjectId: String
)
