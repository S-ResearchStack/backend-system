package researchstack.backend.application.port.incoming.data

import researchstack.backend.domain.data.QueryResult

interface ExecuteDataQueryUseCase {
    suspend fun execute(
        studyId: String,
        databaseName: String,
        command: ExecuteDataQueryCommand
    ): QueryResult
}
