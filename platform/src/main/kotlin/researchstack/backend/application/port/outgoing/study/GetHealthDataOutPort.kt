package researchstack.backend.application.port.outgoing.study

import researchstack.backend.domain.study.HealthDataGroup

interface GetHealthDataOutPort {
    suspend fun getHealthDataList(): List<HealthDataGroup>
}
