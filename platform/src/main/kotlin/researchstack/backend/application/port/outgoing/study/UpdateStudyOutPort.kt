package researchstack.backend.application.port.outgoing.study

import researchstack.backend.domain.study.Study

interface UpdateStudyOutPort {
    suspend fun updateStudy(study: Study)
}
