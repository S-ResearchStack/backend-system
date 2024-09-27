package researchstack.backend.application.port.outgoing.education

import researchstack.backend.domain.education.EducationalContent
import researchstack.backend.enums.EducationalContentStatus

interface GetEducationalContentOutPort {
    suspend fun getEducationalContent(contentId: String): EducationalContent
    suspend fun getEducationalContentList(): List<EducationalContent>
    suspend fun getEducationalContentList(status: EducationalContentStatus): List<EducationalContent>
}
