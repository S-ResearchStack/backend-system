package researchstack.backend.application.port.incoming.study

interface ParticipateInStudyUseCase {
    suspend fun participateInStudy(subjectId: String, studyId: String, command: ParticipateInStudyCommand): String
}
