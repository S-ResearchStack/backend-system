package researchstack.backend.application.port.outgoing.study

interface CreateSubjectNumberGeneratorOutPort {
    suspend fun createSubjectNumberGenerator(studyId: String)
}
