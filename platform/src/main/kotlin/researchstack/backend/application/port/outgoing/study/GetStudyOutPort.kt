package researchstack.backend.application.port.outgoing.study

import researchstack.backend.domain.study.Study

interface GetStudyOutPort {
    suspend fun getStudy(studyId: String): Study
    suspend fun getStudyByParticipationCode(participationCode: String): Study
    suspend fun getStudyList(): List<Study>
    suspend fun getPublicStudyList(): List<Study>
    suspend fun getParticipatedStudyList(subjectId: String): List<Study>
}
