package researchstack.backend.application.port.incoming.studydata

interface AddStudyDataUseCase {
    suspend fun addStudyDataInfo(
        studyId: String,
        studyDataCommand: AddStudyDataCommand
    )

    suspend fun addStudyDataFileInfo(
        studyId: String,
        filePath: String,
        fileName: String
    )
}
