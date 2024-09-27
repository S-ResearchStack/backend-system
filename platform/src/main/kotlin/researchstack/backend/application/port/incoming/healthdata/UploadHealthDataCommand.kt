package researchstack.backend.application.port.incoming.healthdata

import researchstack.backend.enums.HealthDataType

data class UploadHealthDataCommand(
    val type: HealthDataType,
    val data: List<Map<String, Any>>
)
