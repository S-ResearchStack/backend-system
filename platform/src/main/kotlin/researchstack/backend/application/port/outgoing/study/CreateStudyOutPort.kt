package researchstack.backend.application.port.outgoing.study

import researchstack.backend.domain.study.Study

interface CreateStudyOutPort {
    suspend fun createStudy(study: Study): String
}
