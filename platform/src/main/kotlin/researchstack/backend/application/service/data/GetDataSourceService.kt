package researchstack.backend.application.service.data

import org.springframework.stereotype.Service
import researchstack.backend.application.port.incoming.data.GetDataSourceUseCase
import researchstack.backend.application.port.outgoing.data.GetDataSourceOutPort

@Service
class GetDataSourceService(
    private val getDataSourceOutPort: GetDataSourceOutPort
) : GetDataSourceUseCase {
    override suspend fun getDatabaseNames(studyId: String): List<String> {
        return getDataSourceOutPort.getDatabaseNames(studyId)
    }

    override suspend fun getTableNames(databaseName: String): List<String> {
        return getDataSourceOutPort.getTableNames(databaseName)
    }
}
