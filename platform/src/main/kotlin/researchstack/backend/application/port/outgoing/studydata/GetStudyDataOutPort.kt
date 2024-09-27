package researchstack.backend.application.port.outgoing.studydata

import researchstack.backend.domain.studydata.StudyDataFile
import researchstack.backend.domain.studydata.StudyDataFolder

interface GetStudyDataOutPort {
    suspend fun getStudyDataFolderList(
        studyId: String,
        parentId: String,
        page: Long?,
        size: Long?
    ): List<StudyDataFolder>

    suspend fun getStudyDataFolderListCount(
        studyId: String,
        parentId: String
    ): Long

    suspend fun getStudyDataFileList(
        studyId: String,
        parentId: String,
        page: Long?,
        size: Long?
    ): List<StudyDataFile>

    suspend fun getStudyDataFileListCount(
        studyId: String,
        parentId: String
    ): Long

    suspend fun verifyParentPresence(id: String): Boolean

    suspend fun verifyStudyDataPresence(studyId: String, parentId: String?, name: String): Boolean

    suspend fun getStudyDataFolderId(studyId: String, parentId: String, name: String): String?
}
