package researchstack.backend.application.service.data

import org.springframework.stereotype.Service
import researchstack.backend.application.port.incoming.data.ExecuteDataQueryCommand
import researchstack.backend.application.port.incoming.data.ExecuteDataQueryUseCase
import researchstack.backend.application.port.outgoing.data.ExecuteDataQueryOutPort
import researchstack.backend.domain.data.QueryResult

@Service
class ExecuteDataQueryService(
    private val executeDataQueryOutPort: ExecuteDataQueryOutPort
) : ExecuteDataQueryUseCase {

    override suspend fun execute(
        studyId: String,
        databaseName: String,
        command: ExecuteDataQueryCommand
    ): QueryResult {
        // TODO: 1. Check investigator has authority to access the study using studyId
        val validatedQuery = executeDataQueryOutPort.validate(command.query)
        return executeDataQueryOutPort.execute(databaseName, validatedQuery)
    }
}
