package researchstack.backend.adapter.outgoing.mongo.entity.studydata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import researchstack.backend.enums.StudyDataType

@Document("studyDataInfo")
data class StudyDataFolderEntity(
    @Id
    val id: String?,
    val name: String,
    val studyId: String,
    val parentId: String?,
    val type: StudyDataType = StudyDataType.FOLDER
)
