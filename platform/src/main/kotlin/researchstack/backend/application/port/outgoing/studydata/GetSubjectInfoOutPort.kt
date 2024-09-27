package researchstack.backend.application.port.outgoing.studydata

import researchstack.backend.domain.subject.SubjectInfo

interface GetSubjectInfoOutPort {
    suspend fun getSubjectInfoList(
        studyId: String,
        page: Long? = null,
        size: Long? = null
    ): List<SubjectInfo>

    suspend fun getSubjectInfoListCount(studyId: String): Long
    suspend fun getSubjectInfo(studyId: String, subjectNumber: String): SubjectInfo
    suspend fun getSubjectInfoBySubjectId(studyId: String, subjectId: String): SubjectInfo
}
