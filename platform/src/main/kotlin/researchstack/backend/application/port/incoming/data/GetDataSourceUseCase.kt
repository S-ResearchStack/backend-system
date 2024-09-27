package researchstack.backend.application.port.incoming.data

interface GetDataSourceUseCase {
    suspend fun getDatabaseNames(studyId: String): List<String>
    suspend fun getTableNames(databaseName: String): List<String>
}
