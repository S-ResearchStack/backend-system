package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

data class Ecg(
    override val id: HealthDataId?,
    override val time: Instant,
    val minThresholdMv: Double,
    val maxThresholdMv: Double,
    val ppg1: Long,
    val ppg2: Long?,
    val ecg1Mv: Double,
    val ecg2Mv: Double,
    val ecg3Mv: Double,
    val ecg4Mv: Double,
    val ecg5Mv: Double,
    val ecg6Mv: Double?,
    val ecg7Mv: Double?,
    val ecg8Mv: Double?,
    val ecg9Mv: Double?,
    val ecg10Mv: Double?,
) : SampleData(id, time, HealthDataType.ECG) {
    companion object {
        fun newEcg(
            time: Instant,
            minThresholdMv: Double,
            maxThresholdMv: Double,
            ppg1: Long,
            ppg2: Long?,
            ecg1Mv: Double,
            ecg2Mv: Double,
            ecg3Mv: Double,
            ecg4Mv: Double,
            ecg5Mv: Double,
            ecg6Mv: Double?,
            ecg7Mv: Double?,
            ecg8Mv: Double?,
            ecg9Mv: Double?,
            ecg10Mv: Double?,
        ): Ecg =
            Ecg(
                null,
                time,
                minThresholdMv,
                maxThresholdMv,
                ppg1,
                ppg2,
                ecg1Mv,
                ecg2Mv,
                ecg3Mv,
                ecg4Mv,
                ecg5Mv,
                ecg6Mv,
                ecg7Mv,
                ecg8Mv,
                ecg9Mv,
                ecg10Mv,
            )
    }
}
