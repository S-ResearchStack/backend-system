package researchstack.backend.application.port.incoming.healthdata

import researchstack.backend.domain.study.HealthDataGroup

interface GetHealthDataUseCase {
    suspend fun getHealthDataList(): List<HealthDataGroup>
}
