package researchstack.backend.adapter.outgoing.mongo.entity.studydata

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import researchstack.backend.enums.StudyDataFileType
import researchstack.backend.enums.StudyDataType

@Document("studyDataInfo")
data class StudyDataFileEntity(
    @Id
    val id: String?,
    val name: String,
    val studyId: String,
    val parentId: String?,
    val type: StudyDataType = StudyDataType.FILE,
    val fileType: StudyDataFileType,
    val filePath: String,
    val fileSize: Number,
    var filePreview: String?,
    val createdAt: String
)
