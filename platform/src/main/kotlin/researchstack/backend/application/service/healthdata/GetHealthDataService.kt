package researchstack.backend.application.service.healthdata

import org.springframework.stereotype.Service
import researchstack.backend.application.port.incoming.healthdata.GetHealthDataUseCase
import researchstack.backend.application.port.outgoing.study.GetHealthDataOutPort
import researchstack.backend.domain.study.HealthDataGroup

@Service
class GetHealthDataService(
    private val getHealthDataOutPort: GetHealthDataOutPort
) : GetHealthDataUseCase {
    override suspend fun getHealthDataList(): List<HealthDataGroup> {
        return getHealthDataOutPort.getHealthDataList()
    }
}
