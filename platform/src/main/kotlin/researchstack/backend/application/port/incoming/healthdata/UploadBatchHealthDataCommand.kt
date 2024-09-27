package researchstack.backend.application.port.incoming.healthdata

import researchstack.backend.enums.HealthDataType

data class UploadBatchHealthDataCommand(
    val batches: List<BatchHealthData>
) {
    data class BatchHealthData(
        val type: HealthDataType,
        val data: List<Map<String, Any>>
    )
}
