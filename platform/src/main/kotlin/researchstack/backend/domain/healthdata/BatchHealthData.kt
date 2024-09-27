package researchstack.backend.domain.healthdata

import researchstack.backend.enums.HealthDataType

data class BatchHealthData(
    val type: HealthDataType,
    val data: List<HealthData>
)
