package researchstack.backend.application.port.incoming.studydata

import researchstack.backend.application.port.incoming.common.PaginationCommand
import researchstack.backend.domain.studydata.StudyDataFolder
import researchstack.backend.enums.StudyDataType

interface GetStudyDataUseCase {
    suspend fun getStudyDataInfoList(
        studyId: String,
        parentId: String,
        studyDataType: StudyDataType,
        paginationCommand: PaginationCommand? = null
    ): List<StudyDataFolder>

    suspend fun getStudyDataInfoListCount(
        studyId: String,
        parentId: String,
        studyDataType: StudyDataType
    ): StudyDataCountResponse
}
