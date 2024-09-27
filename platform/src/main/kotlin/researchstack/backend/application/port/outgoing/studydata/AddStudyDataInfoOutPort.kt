package researchstack.backend.application.port.outgoing.studydata

import researchstack.backend.domain.studydata.StudyDataFolder

interface AddStudyDataInfoOutPort {
    suspend fun addStudyDataInfo(studyDataFolder: StudyDataFolder): Unit
}
