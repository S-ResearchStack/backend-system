package researchstack.backend.application.port.outgoing.education

interface DeleteEducationalContentOutPort {
    suspend fun deleteEducationalContent(contentId: String)
}
