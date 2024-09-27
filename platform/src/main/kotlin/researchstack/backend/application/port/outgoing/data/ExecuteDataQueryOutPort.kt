package researchstack.backend.application.port.outgoing.data

import researchstack.backend.domain.data.QueryResult

interface ExecuteDataQueryOutPort {
    suspend fun validate(query: String): String
    suspend fun execute(databaseName: String, query: String): QueryResult
}
