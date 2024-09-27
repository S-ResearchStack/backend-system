package researchstack.backend.application.port.outgoing.education

import researchstack.backend.domain.education.EducationalContent

interface CreateEducationalContentOutPort {
    suspend fun createEducationalContent(educationalContent: EducationalContent): EducationalContent
}
