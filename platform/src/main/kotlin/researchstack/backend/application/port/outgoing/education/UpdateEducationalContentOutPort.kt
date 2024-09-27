package researchstack.backend.application.port.outgoing.education

import researchstack.backend.domain.education.EducationalContent

interface UpdateEducationalContentOutPort {
    suspend fun updateEducationalContent(educationalContent: EducationalContent)
}
