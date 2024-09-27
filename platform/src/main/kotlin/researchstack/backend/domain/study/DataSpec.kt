package researchstack.backend.domain.study

data class DataSpec(
    val dataId: String,
    val dataName: String,
    val dataDescription: String,
    val collectionMethod: String,
    val targetTrialNumber: Long,
    val durationThreshold: Long
)
