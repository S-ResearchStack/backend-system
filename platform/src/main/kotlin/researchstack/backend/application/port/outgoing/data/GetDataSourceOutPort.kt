package researchstack.backend.application.port.outgoing.data

interface GetDataSourceOutPort {
    suspend fun getDatabaseNames(studyId: String): List<String>
    suspend fun getTableNames(databaseName: String): List<String>
}
