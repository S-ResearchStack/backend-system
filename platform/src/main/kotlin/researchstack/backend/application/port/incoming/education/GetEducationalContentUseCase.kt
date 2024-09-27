package researchstack.backend.application.port.incoming.education

import researchstack.backend.domain.education.EducationalContent
import researchstack.backend.enums.EducationalContentStatus

interface GetEducationalContentUseCase {
    suspend fun getEducationalContent(studyId: String, contentId: String): EducationalContent
    suspend fun getEducationalContentList(
        studyId: String,
        status: EducationalContentStatus? = null
    ): List<EducationalContent>
}
