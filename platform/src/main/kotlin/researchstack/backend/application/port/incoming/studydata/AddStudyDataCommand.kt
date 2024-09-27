package researchstack.backend.application.port.incoming.studydata

import researchstack.backend.enums.StudyDataFileType
import researchstack.backend.enums.StudyDataType
import java.time.ZonedDateTime

data class AddStudyDataCommand(
    val name: String,
    val type: StudyDataType,
    val parentId: String,
    val fileInfo: FileDataInfo?
) {
    data class FileDataInfo(
        val fileType: StudyDataFileType,
        val filePath: String,
        val fileSize: Number,
        var filePreview: String?,
        val createdAt: ZonedDateTime = ZonedDateTime.now()
    )
}
