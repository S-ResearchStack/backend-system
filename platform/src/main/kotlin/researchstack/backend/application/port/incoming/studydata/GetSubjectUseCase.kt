package researchstack.backend.application.port.incoming.studydata

import researchstack.backend.application.port.incoming.common.PaginationCommand

interface GetSubjectUseCase {
    suspend fun getSubjectInfoList(
        studyId: String,
        includeTaskProgress: Boolean? = false,
        paginationCommand: PaginationCommand? = null
    ): List<SubjectInfoResponse>

    suspend fun getSubjectInfoListCount(studyId: String): StudyDataCountResponse
}
