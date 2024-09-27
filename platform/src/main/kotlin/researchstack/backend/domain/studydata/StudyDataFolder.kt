package researchstack.backend.domain.studydata

import researchstack.backend.enums.StudyDataFileType
import researchstack.backend.enums.StudyDataType
import java.time.ZonedDateTime

open class StudyDataFolder(
    open val id: String? = null,
    open val name: String,
    open val studyId: String,
    open val parentId: String?,
    open val type: StudyDataType = StudyDataType.FOLDER
)

data class StudyDataFile(
    override val id: String? = null,
    override val name: String,
    override val studyId: String,
    override val parentId: String?,
    override val type: StudyDataType = StudyDataType.FILE,
    val fileType: StudyDataFileType,
    val filePath: String,
    val fileSize: Number,
    var filePreview: String?,
    val createdAt: ZonedDateTime = ZonedDateTime.now()
) : StudyDataFolder(
    name = name,
    studyId = studyId,
    parentId = parentId,
    type = StudyDataType.FILE
)
